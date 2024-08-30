package com.baedalping.delivery.domain.store.dto;

import com.baedalping.delivery.domain.store.entity.Store;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateResponseDto {
  private UUID storeId;
  private String storeName;
  private String storePhone;
  private String storeAddress;
  private String storeDetail;
  private LocalTime openTime;
  private LocalTime closeTime;
  private UUID storeCategoryId;

  public StoreUpdateResponseDto(Store store){
    this.storeId = store.getStoreId();
    this.storeName = store.getStoreName();
    this.storePhone = store.getStorePhone();
    this.storeAddress = store.getStoreAddress();
    this.storeDetail = store.getStoreDetail();
    this.openTime = store.getOpenTime();
    this.closeTime = store.getCloseTime();
    this.storeCategoryId = store.getStoreCategory().getStoreCategoryId();
  }


}
