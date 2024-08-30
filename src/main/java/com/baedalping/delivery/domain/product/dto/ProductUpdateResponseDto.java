package com.baedalping.delivery.domain.product.dto;

import com.baedalping.delivery.domain.product.entity.Product;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateResponseDto {
  private UUID productId;
  private String productName;
  private Integer productPrice;
  private String productDetail;
  private String productImgUrl;
  private UUID storeId;
  private UUID categoryId;

  //여기서 수정 요청이 하나만 올 경우 빌더패턴을 사용해야되는지 물어보기
  public ProductUpdateResponseDto(Product product){
    this.productId = product.getProductId();
    this.productName = product.getProductName();
    this.productPrice = product.getProductPrice();
    this.productDetail = product.getProductDetail();
    this.productImgUrl = product.getProductImgUrl();
    this.storeId = product.getStore().getStoreId();
    this.categoryId = product.getProductCategory().getProductCategoryId();
  }
}
