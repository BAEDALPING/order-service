package com.baedalping.delivery.domain.product.controller;

import com.baedalping.delivery.domain.product.dto.ProductCreateRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductCreateResponseDto;
import com.baedalping.delivery.domain.product.service.ProductService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ApiResponse<ProductCreateResponseDto> createProduct(@RequestBody ProductCreateRequestDto productCreateRequestDto){
    return ApiResponse.created(productService.createProduct(productCreateRequestDto));
  }
}
