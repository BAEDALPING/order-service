package com.baedalping.delivery.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
  @NotBlank(message = "상품명을 입력해주세요.")
  private String productName;
  @NotBlank(message = "상품 가격을 입력해주세요.")
  private Integer productPrice;
  private String productDetail;
  private String productImgUrl;
  private UUID storeId;
  private UUID productCategoryId;
}
