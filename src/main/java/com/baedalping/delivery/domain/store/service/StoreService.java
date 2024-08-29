package com.baedalping.delivery.domain.store.service;


import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.domain.store.dto.StoreCreateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreCreateResponseDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateResponseDto;
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
  public StoreCreateResponseDto createStore(StoreCreateRequestDto storeCreateRequestDto) {
    StoreCategory storeCategory = storeCategoryService.findById(storeCreateRequestDto.getStoreCategoryId());
    Store store =  storeRepository.save(new Store(storeCreateRequestDto, storeCategory));
    return new StoreCreateResponseDto(store);
  }

  @Transactional
  public StoreUpdateResponseDto updateStore(UUID storeId, StoreUpdateRequestDto storeUpdateRequestDto) {
    Store store = findById(storeId);
    StoreCategory storeCategory = storeCategoryService.findById(storeUpdateRequestDto.getStoreCategoryId());
    store.updateStore(storeUpdateRequestDto, storeCategory);
    return new StoreUpdateResponseDto(store);
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
  public StoreCreateResponseDto getStore(UUID storeId) {
    return new StoreCreateResponseDto(findById(storeId));
  }

  @Transactional
  public List<StoreCreateResponseDto> getStoresByStoreCategoryId(UUID storeCategoryId) {
    StoreCategory storeCategory = storeCategoryService.findById(storeCategoryId);
    List<Store> storeList = storeRepository.findAllByStoreCategory(storeCategory);

    return storeList.stream()
        .map(StoreCreateResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<StoreCreateResponseDto> getStores(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Store> storeList = storeRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return storeList.map(StoreCreateResponseDto::new);
  }

  @Transactional
  public List<StoreCreateResponseDto> getStoreSearch(String keyword) {
    List<Store> storeList = storeRepository.findAllByStoreNameContaining(keyword);
    return storeList.stream()
        .map(StoreCreateResponseDto::new)
        .collect(Collectors.toList());
  }


  public Store findById(UUID storeId) {
    return storeRepository.findById(storeId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
    );
  }

}
