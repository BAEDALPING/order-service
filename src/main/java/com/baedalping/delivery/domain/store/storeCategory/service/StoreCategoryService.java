package com.baedalping.delivery.domain.store.storeCategory.service;

import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.repository.StoreCategoryRepository;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StoreCategoryService {

  private final StoreCategoryRepository storeCategoryRepository;

  @Transactional
  public StoreCategoryCreateResponseDto createStoreCategory(StoreCategoryCreateRequestDto storeCategoryCreateRequestDto) {
    String storeCategoryName = storeCategoryCreateRequestDto.getStoreCategoryName();
    if(storeCategoryRepository.findByStoreCategoryName(storeCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_STORE_CATEGORY_NAME);
    }
    
    StoreCategory storeCategory = storeCategoryRepository.save(new StoreCategory(storeCategoryCreateRequestDto));
    return new StoreCategoryCreateResponseDto(storeCategory);
  }

  @Transactional
  public StoreCategoryUpdateResponseDto updateStoreCatgegory(UUID storeCategoryId, StoreCategoryUpdateRequestDto storeCategoryUpdateRequestDto) {
    StoreCategory storeCategory = storeCategoryRepository.findById(storeCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );

    storeCategory.setStoreCategoryName(storeCategoryUpdateRequestDto.getStoreCategoryName());
    StoreCategory updatedStoreCategory = storeCategoryRepository.save(storeCategory);
    return new StoreCategoryUpdateResponseDto(updatedStoreCategory);
  }
}
