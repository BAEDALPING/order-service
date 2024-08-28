package com.baedalping.delivery.domain.productCategory.dto;

import com.baedalping.delivery.domain.productCategory.entity.ProductCategory;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategoryUpdateResponseDto {
  private UUID productCategoryId;
  private String productCategoryName;

  public ProductCategoryUpdateResponseDto(ProductCategory productCategory){
    this.productCategoryId = productCategory.getProductCategoryId();
    this.productCategoryName = productCategory.getProductCategoryName();
  }
}
