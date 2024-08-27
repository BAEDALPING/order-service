package com.baedalping.delivery.store;

import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StoreCreateRequestDto {
  private String storeName;
  private String storePhone;
  private String storeAddress;
  private String storeDetail;
  private LocalTime openTime;
  private LocalTime closeTime;
  private UUID storeCategoryId;
}
