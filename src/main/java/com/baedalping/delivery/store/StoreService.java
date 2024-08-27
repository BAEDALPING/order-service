package com.baedalping.delivery.store;


import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.storeCategory.StoreCategory;
import com.baedalping.delivery.storeCategory.StoreCategoryRepository;
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
