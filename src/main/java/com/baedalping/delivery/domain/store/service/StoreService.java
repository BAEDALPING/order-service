package com.baedalping.delivery.domain.store.service;


import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.domain.store.dto.StoreRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreResponseDto;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import com.baedalping.delivery.domain.store.storeCategory.service.StoreCategoryService;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;
  private final StoreCategoryService storeCategoryService;

  @Transactional
  public StoreResponseDto createStore(StoreRequestDto storeRequestDto) {
    StoreCategory storeCategory = storeCategoryService.findById(storeRequestDto.getStoreCategoryId());
    Store store =  storeRepository.save(new Store(storeRequestDto, storeCategory));
    return new StoreResponseDto(store);
  }

  @Transactional
  public StoreResponseDto updateStore(UUID storeId, StoreRequestDto StoreRequestDto) {
    Store store = findById(storeId);
    StoreCategory storeCategory = storeCategoryService.findById(StoreRequestDto.getStoreCategoryId());
    store.updateStore(StoreRequestDto, storeCategory);
    return new StoreResponseDto(store);
  }

  @Transactional
  public void deleteStore(UUID storeId) {
    Store store = findById(storeId);

    List<Product> productList = productRepository.findAllByStore(store);
    for(Product product : productList){
      product.delete(null);
    }

    store.delete(null);
  }

  @Transactional
  public StoreResponseDto getStore(UUID storeId) {
    return new StoreResponseDto(findById(storeId));
  }

  @Transactional
  public List<StoreResponseDto> getStoresByStoreCategoryId(UUID storeCategoryId) {
    StoreCategory storeCategory = storeCategoryService.findById(storeCategoryId);
    List<Store> storeList = storeRepository.findAllByStoreCategory(storeCategory);

    return storeList.stream()
        .map(StoreResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<StoreResponseDto> getStores(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Store> storeList = storeRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return storeList.map(StoreResponseDto::new);
  }

  @Transactional
  public List<StoreResponseDto> getStoreSearch(String keyword) {
    List<Store> storeList = storeRepository.findAllByStoreNameContaining(keyword);
    return storeList.stream()
        .map(StoreResponseDto::new)
        .collect(Collectors.toList());
  }


  public Store findById(UUID storeId) {
    return storeRepository.findById(storeId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
    );
  }

}
