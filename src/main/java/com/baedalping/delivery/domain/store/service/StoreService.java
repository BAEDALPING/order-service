package com.baedalping.delivery.domain.store.service;


import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.domain.store.dto.StoreCreateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreCreateResponseDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateResponseDto;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import com.baedalping.delivery.domain.store.storeCategory.repository.StoreCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final StoreCategoryRepository storeCategoryRepository;
  private final ProductRepository productRepository;

  @Transactional
  public StoreCreateResponseDto createStore(StoreCreateRequestDto storeCreateRequestDto) {
    StoreCategory storeCategory = storeCategoryRepository.findById(storeCreateRequestDto.getStoreCategoryId()).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );

    Store store =  storeRepository.save(new Store(storeCreateRequestDto, storeCategory));
    return new StoreCreateResponseDto(store);
  }

  @Transactional
  public StoreUpdateResponseDto updateStore(UUID storeId, StoreUpdateRequestDto storeUpdateRequestDto) {
    Store store = storeRepository.findById(storeId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
    );

    StoreCategory storeCategory = storeCategoryRepository.findById(storeUpdateRequestDto.getStoreCategoryId()).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );

    store.updateStore(storeUpdateRequestDto, storeCategory);
    return new StoreUpdateResponseDto(store);
  }

  @Transactional
  public void deleteStore(UUID storeId) {
    Store store = storeRepository.findById(storeId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
    );

    List<Product> productList = productRepository.findAllByStore(store);
    for(Product product : productList){
      product.delete(null);
    }

    store.delete(null);
  }
}
