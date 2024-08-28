package com.baedalping.delivery.domain.store.service;


import com.baedalping.delivery.domain.store.dto.StoreCreateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreCreateResponseDto;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.domain.storeCategory.entity.StoreCategory;
import com.baedalping.delivery.domain.storeCategory.repository.StoreCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final StoreCategoryRepository storeCategoryRepository;

  @Transactional
  public StoreCreateResponseDto createStore(StoreCreateRequestDto storeCreateRequestDto) {
    UUID storeCategoryId = storeCreateRequestDto.getStoreCategoryId();
    StoreCategory storeCategory = storeCategoryRepository.findById(storeCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );

    Store store =  storeRepository.save(new Store(storeCreateRequestDto, storeCategory));
    return new StoreCreateResponseDto(store);
  }




}
