package com.baedalping.delivery.domain.store.storeCategory.controller;


import com.baedalping.delivery.domain.store.dto.StoreResponseDto;
import com.baedalping.delivery.domain.store.service.StoreService;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.service.StoreCategoryService;
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

@RequestMapping("/storeCategories")
@RequiredArgsConstructor
@RestController
public class StoreCategoryController {

  private final StoreCategoryService storeCategoryService;
  private final StoreService storeService;

  @PostMapping
  public ApiResponse<StoreCategoryResponseDto> createStoreCategory(@Valid @RequestBody StoreCategoryRequestDto storeCategoryRequestDto){
    return ApiResponse.created(storeCategoryService.createStoreCategory(storeCategoryRequestDto));
  }

  @PutMapping("/{storeCategoryId}")
  public ApiResponse<StoreCategoryResponseDto> updateStoreCategory(
      @PathVariable("storeCategoryId") UUID storeCategoryId,
      @Valid @RequestBody StoreCategoryRequestDto storeCategoryRequestDto
  ){
    return ApiResponse.ok(storeCategoryService.updateStoreCategory(storeCategoryId, storeCategoryRequestDto));
  }

  @DeleteMapping("/{storeCategoryId}")
  public ApiResponse deleteStoreCategory(@PathVariable("storeCategoryId") UUID storeCategoryId){
    storeCategoryService.deleteStoreCategory(storeCategoryId);
    return ApiResponse.ok(null);
  }

  //가게 분류 전체 조회
  @GetMapping
  public ApiResponse<Page<StoreCategoryResponseDto>> getStoreCategories(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
      ) {
    return ApiResponse.ok(storeCategoryService.getStoreCategories(page, size, sortBy, isAsc));
  }


  //가게 분류별 가게 조회
  @GetMapping("/{storeCategoryId}")
  public ApiResponse<List<StoreResponseDto>> getStoresByStoreCategoryId(
      @PathVariable("storeCategoryId") UUID storeCategoryId){
    return ApiResponse.ok(storeService.getStoresByStoreCategoryId(storeCategoryId));
  }


}
