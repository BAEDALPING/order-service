package com.baedalping.delivery.domain.product.productCategory.controller;

import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryCreateRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryCreateResponseDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryUpdateRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryUpdateResponseDto;
import com.baedalping.delivery.domain.product.productCategory.service.ProductCategoryService;
import com.baedalping.delivery.global.common.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/productCategories")
@RequiredArgsConstructor
@RestController
public class ProductCategoryController {

  private final ProductCategoryService productCategoryService;

  @PostMapping
  public ApiResponse<ProductCategoryCreateResponseDto> createProductCategory(@RequestBody ProductCategoryCreateRequestDto productCategoryCreateRequestDto){
    return ApiResponse.created(productCategoryService.createProductCategory(productCategoryCreateRequestDto));
  }

  @PutMapping("{productCategoryId}")
  public ApiResponse<ProductCategoryUpdateResponseDto> updateProductCatgegory(
      @PathVariable("productCategoryId") UUID productCategoryId,
      @RequestBody ProductCategoryUpdateRequestDto productCategoryUpdateRequestDto
  ){
    return ApiResponse.ok(productCategoryService.updateProductCatgegory(productCategoryId, productCategoryUpdateRequestDto));
  }

  @DeleteMapping("{productCategoryId}")
  public ApiResponse deleteProductCatgegory(@PathVariable("productCategoryId") UUID productCategoryId){
    productCategoryService.deleteProductCatgegory(productCategoryId);
    return ApiResponse.ok(null);
  }
}
