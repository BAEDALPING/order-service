package com.baedalping.delivery.domain.product.dto;

import com.baedalping.delivery.domain.product.entity.Product;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
  private UUID productId;
  private String productName;
  private Integer productPrice;
  private String productDetail;
  private String productImgUrl;
  private UUID storeId;
  private UUID categoryId;

  public ProductResponseDto(Product product){
    this.productId = product.getProductId();
    this.productName = product.getProductName();
    this.productPrice = product.getProductPrice();
    this.productDetail = product.getProductDetail();
    this.productImgUrl = product.getProductImgUrl();
    this.storeId = product.getStore().getStoreId();
    this.categoryId = product.getProductCategory().getProductCategoryId();
  }
}
