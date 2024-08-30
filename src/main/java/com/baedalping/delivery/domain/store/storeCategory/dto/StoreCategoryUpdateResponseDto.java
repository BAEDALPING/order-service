package com.baedalping.delivery.domain.store.storeCategory.dto;

import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreCategoryUpdateResponseDto {
  private UUID storeCategoryId;
  private String storeCategoryName;

  public StoreCategoryUpdateResponseDto(StoreCategory storeCategory){
    this.storeCategoryId = storeCategory.getStoreCategoryId();
    this.storeCategoryName = storeCategory.getStoreCategoryName();
  }
}
