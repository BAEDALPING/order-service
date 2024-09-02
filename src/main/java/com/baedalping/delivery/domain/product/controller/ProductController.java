package com.baedalping.delivery.domain.product.controller;

import com.baedalping.delivery.domain.product.dto.ProductRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductResponseDto;
import com.baedalping.delivery.domain.product.service.ProductService;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public ApiResponse<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto){
    return ApiResponse.created(productService.createProduct(productRequestDto));
  }

  @PutMapping("/{productId}")
  public ApiResponse<ProductResponseDto> updateProduct(
      @PathVariable("productId") UUID productId,
      @Valid @RequestBody ProductRequestDto productRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ){
    return ApiResponse.ok(productService.updateProduct(productId, productRequestDto, userDetails.getUserId()));
  }

  @DeleteMapping("/{productId}")
  public ApiResponse deleteProduct(
      @PathVariable("productId") UUID productId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ){
    productService.deleteProduct(productId, userDetails.getUserId());
    return ApiResponse.ok(null);
  }

  @GetMapping("/{productId}")
  public ApiResponse<ProductResponseDto> getProduct(@PathVariable("productId") UUID productId){
    return ApiResponse.ok(productService.getProduct(productId));
  }

  //상품 전체 조회
  @GetMapping
  public ApiResponse<Page<ProductResponseDto>> getProducts(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
  ) {
    return ApiResponse.ok(productService.getProducts(page, size, sortBy, isAsc));
  }

  //상품 검색
  @GetMapping("/search")
  public ApiResponse<List<ProductResponseDto>> getProductSearch(@RequestParam("keyword") String keyword){
    return ApiResponse.ok(productService.getProductSearch(keyword));

  }


}
