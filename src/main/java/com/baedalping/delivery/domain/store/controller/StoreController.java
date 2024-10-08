package com.baedalping.delivery.domain.store.controller;

import com.baedalping.delivery.domain.product.dto.ProductResponseDto;
import com.baedalping.delivery.domain.product.service.ProductService;
import com.baedalping.delivery.domain.store.dto.StoreRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreResponseDto;
import com.baedalping.delivery.domain.store.service.StoreService;
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


@RequestMapping("/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {

  private final StoreService storeService;
  private final ProductService productService;

  @PostMapping
  public ApiResponse<StoreResponseDto> createStore(
      @Valid @RequestBody StoreRequestDto storeRequestDto
  ){
    return ApiResponse.created(storeService.createStore(storeRequestDto));
  }

  @PutMapping("/{storeId}")
  public ApiResponse<StoreResponseDto> updateStore(
      @PathVariable("storeId") UUID storeId,
      @Valid @RequestBody StoreRequestDto StoreRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ){
    return ApiResponse.ok(storeService.updateStore(storeId, StoreRequestDto, userDetails.getUserId()));
  }

  @DeleteMapping("/{storeId}")
  public ApiResponse deleteStore(@PathVariable("storeId") UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ){
    storeService.deleteStore(storeId, userDetails.getUserId());
    return ApiResponse.ok(null);
  }

  @GetMapping("/{storeId}")
  public ApiResponse<StoreResponseDto> getStore(@PathVariable("storeId") UUID storeId){
    return ApiResponse.ok(storeService.getStore(storeId));
  }

  //가게 전체 조회
  @GetMapping
  public ApiResponse<Page<StoreResponseDto>> getStores(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
  ) {
    return ApiResponse.ok(storeService.getStores(page, size, sortBy, isAsc));
  }

  //가게별 상품 조회
  @GetMapping("/{storeId}/products")
  public ApiResponse<List<ProductResponseDto>> getProductByStoreId(
      @PathVariable("storeId") UUID storeId){
    return ApiResponse.ok(productService.getProductByStoreId(storeId));
  }

  //가게 검색
  @GetMapping("/search")
  public ApiResponse<List<StoreResponseDto>> getStoreSearch(@RequestParam("keyword") String keyword){
    return ApiResponse.ok(storeService.getStoreSearch(keyword));
  }


}
