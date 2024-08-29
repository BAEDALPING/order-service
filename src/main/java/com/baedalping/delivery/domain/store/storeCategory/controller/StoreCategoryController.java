package com.baedalping.delivery.domain.store.storeCategory.controller;


import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.service.StoreCategoryService;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/storeCategories")
@RequiredArgsConstructor
@RestController
public class StoreCategoryController {

  private final StoreCategoryService storeCategoryService;

  @PostMapping
  public ApiResponse<StoreCategoryCreateResponseDto> createStoreCategory(@Valid @RequestBody StoreCategoryCreateRequestDto storeCategoryCreateRequestDto){
    return ApiResponse.created(storeCategoryService.createStoreCategory(storeCategoryCreateRequestDto));
  }

  @PutMapping("{storeCategoryId}")
  public ApiResponse<StoreCategoryUpdateResponseDto> updateStoreCategory(
      @PathVariable("storeCategoryId") UUID storeCategoryId,
      @Valid @RequestBody StoreCategoryUpdateRequestDto storeCategoryUpdateRequestDto
  ){
    return ApiResponse.ok(storeCategoryService.updateStoreCategory(storeCategoryId, storeCategoryUpdateRequestDto));
  }

  @DeleteMapping("{storeCategoryId}")
  public ApiResponse deleteStoreCategory(@PathVariable("storeCategoryId") UUID storeCategoryId){
    storeCategoryService.deleteStoreCategory(storeCategoryId);
    return ApiResponse.ok(null);
  }
}
