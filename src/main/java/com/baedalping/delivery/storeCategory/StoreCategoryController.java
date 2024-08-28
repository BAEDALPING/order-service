package com.baedalping.delivery.storeCategory;

import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/storeCategories")
@RequiredArgsConstructor
@RestController
public class StoreCategoryController {

  private final StoreCategoryService storeCategoryService;

  @PostMapping
  public ApiResponse<StoreCategoryCreateResponseDto> createStoreCategory(@RequestBody StoreCategoryCreateRequestDto storeCategoryCreateRequestDto){
    return ApiResponse.created(storeCategoryService.createStoreCategory(storeCategoryCreateRequestDto));
  }

}
