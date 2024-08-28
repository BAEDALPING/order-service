package com.baedalping.delivery.productCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductCategoryCreateRequestDto {
  @NotBlank(message = "상품 분류명을 입력해주세요.")
  private String productCategoryName;
}
