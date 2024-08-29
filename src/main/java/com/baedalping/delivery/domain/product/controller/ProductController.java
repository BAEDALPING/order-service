package com.baedalping.delivery.domain.product.controller;

import com.baedalping.delivery.domain.product.dto.ProductCreateRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductCreateResponseDto;
import com.baedalping.delivery.domain.product.dto.ProductUpdateRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductUpdateResponseDto;
import com.baedalping.delivery.domain.product.service.ProductService;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ApiResponse<ProductCreateResponseDto> createProduct(@Valid @RequestBody ProductCreateRequestDto productCreateRequestDto){
    return ApiResponse.created(productService.createProduct(productCreateRequestDto));
  }

  @PutMapping("/{productId}")
  public ApiResponse<ProductUpdateResponseDto> updateProduct(
      @PathVariable("productId") UUID productId,
      @Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto
  ){
    return ApiResponse.ok(productService.updateProduct(productId, productUpdateRequestDto));
  }

  @DeleteMapping("/{productId}")
  public ApiResponse deleteProduct(@PathVariable("productId") UUID productId){
    productService.deleteProduct(productId);
    return ApiResponse.ok(null);
  }

  @GetMapping("/{productId}")
  public ApiResponse<ProductCreateResponseDto> getProduct(@PathVariable("productId") UUID productId){
    return ApiResponse.ok(productService.getProduct(productId));
  }

  //상품 전체 조회
  @GetMapping
  public ApiResponse<Page<ProductCreateResponseDto>> getProducts(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
  ) {
    return ApiResponse.ok(productService.getProducts(page, size, sortBy, isAsc));
  }

  //상품 검색
  @GetMapping("/search")
  public ApiResponse<List<ProductCreateResponseDto>> getProductSearch(@RequestParam("keyword") String keyword){
    return ApiResponse.ok(productService.getProductSearch(keyword));

  }


}
