package com.baedalping.delivery.domain.storeCategory.service;

import com.baedalping.delivery.domain.storeCategory.dto.StoreCategoryCreateRequestDto;
import com.baedalping.delivery.domain.storeCategory.dto.StoreCategoryCreateResponseDto;
import com.baedalping.delivery.domain.storeCategory.repository.StoreCategoryRepository;
import com.baedalping.delivery.domain.storeCategory.entity.StoreCategory;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StoreCategoryService {

  private final StoreCategoryRepository storeCategoryRepository;

  @Transactional
  public StoreCategoryCreateResponseDto createStoreCategory(
      StoreCategoryCreateRequestDto storeCategoryCreateRequestDto) {
    String storeCategoryName = storeCategoryCreateRequestDto.getStoreCategoryName();
    if(storeCategoryRepository.findByStoreCategoryName(storeCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_STORE_CATEGORY_NAME);
    }
    
    StoreCategory storeCategory = storeCategoryRepository.save(new StoreCategory(storeCategoryCreateRequestDto));
    return new StoreCategoryCreateResponseDto(storeCategory);
  }
}
