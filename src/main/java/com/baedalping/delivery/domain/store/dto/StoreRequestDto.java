package com.baedalping.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StoreRequestDto {
  @NotBlank(message = "상호명을 입력해주세요.")
  private String storeName;
  private String storePhone;
  private String storeAddress;
  private String storeDetail;
  private LocalTime openTime;
  private LocalTime closeTime;
  private UUID storeCategoryId;
}
