package com.baedalping.delivery.domain.storeCategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StoreCategoryCreateRequestDto {
  @NotBlank(message = "상품 분류명을 입력해주세요.")
  private String storeCategoryName;
}
