package com.baedalping.delivery.domain.product.productCategory.dto;

import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategoryCreateResponseDto {
  private UUID productCategoryId;
  private String productCategoryName;

  public ProductCategoryCreateResponseDto(ProductCategory productCategory){
    this.productCategoryId = productCategory.getProductCategoryId();
    this.productCategoryName = productCategory.getProductCategoryName();
  }
}
