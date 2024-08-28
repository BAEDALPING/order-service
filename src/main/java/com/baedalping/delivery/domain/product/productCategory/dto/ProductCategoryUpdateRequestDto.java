package com.baedalping.delivery.domain.product.productCategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductCategoryUpdateRequestDto {
  @NotBlank(message = "상품 분류명을 입력해주세요.")
  private String productCategoryName;
}
