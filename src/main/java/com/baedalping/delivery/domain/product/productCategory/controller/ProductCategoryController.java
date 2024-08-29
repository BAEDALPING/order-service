package com.baedalping.delivery.domain.product.productCategory.controller;

import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryResponseDto;
import com.baedalping.delivery.domain.product.productCategory.service.ProductCategoryService;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/productCategories")
@RequiredArgsConstructor
@RestController
public class ProductCategoryController {

  private final ProductCategoryService productCategoryService;

  @PostMapping
  public ApiResponse<ProductCategoryResponseDto> createProductCategory(@Valid @RequestBody ProductCategoryRequestDto productCategoryRequestDto){
    return ApiResponse.created(productCategoryService.createProductCategory(productCategoryRequestDto));
  }

  @PutMapping("/{productCategoryId}")
  public ApiResponse<ProductCategoryResponseDto> updateProductCatgegory(
      @PathVariable("productCategoryId") UUID productCategoryId,
      @Valid @RequestBody ProductCategoryRequestDto productCategoryRequestDto
  ){
    return ApiResponse.ok(productCategoryService.updateProductCatgegory(productCategoryId, productCategoryRequestDto));
  }

  @DeleteMapping("/{productCategoryId}")
  public ApiResponse deleteProductCatgegory(@PathVariable("productCategoryId") UUID productCategoryId){
    productCategoryService.deleteProductCatgegory(productCategoryId);
    return ApiResponse.ok(null);
  }

  //상품 분류 전체 조회
  @GetMapping
  public ApiResponse<Page<ProductCategoryResponseDto>> getProductCategories(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
  ) {
    return ApiResponse.ok(productCategoryService.getProductCategories(page, size, sortBy, isAsc));
  }
}
