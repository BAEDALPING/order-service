package com.baedalping.delivery.domain.productCategory.controller;

import com.baedalping.delivery.domain.productCategory.dto.ProductCategoryCreateRequestDto;
import com.baedalping.delivery.domain.productCategory.dto.ProductCategoryCreateResponseDto;
import com.baedalping.delivery.domain.productCategory.service.ProductCategoryService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
}
