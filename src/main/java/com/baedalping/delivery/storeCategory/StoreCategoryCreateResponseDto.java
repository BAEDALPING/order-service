package com.baedalping.delivery.storeCategory;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreCategoryCreateResponseDto {
  private UUID storeCategoryId;
  private String storeCategoryName;

  public StoreCategoryCreateResponseDto(StoreCategory storeCategory){
    this.storeCategoryId = storeCategory.getStoreCategoryId();
    this.storeCategoryName = storeCategory.getStoreCategoryName();
  }
}
