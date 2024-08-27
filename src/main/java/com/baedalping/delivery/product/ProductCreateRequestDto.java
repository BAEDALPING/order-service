package com.baedalping.delivery.product;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
  private String productName;
  private Integer productPrice;
  private String productDetail;
  private String productImgUrl;
  private UUID storeId;
  private UUID productCategoryId;
}
