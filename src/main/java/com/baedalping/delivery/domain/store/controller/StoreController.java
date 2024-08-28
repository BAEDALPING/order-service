package com.baedalping.delivery.domain.store.controller;

import com.baedalping.delivery.domain.store.dto.StoreCreateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreCreateResponseDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.baedalping.delivery.domain.store.dto.StoreUpdateResponseDto;
import com.baedalping.delivery.domain.store.service.StoreService;
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


@RequestMapping("/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {

  private final StoreService storeService;

  @PostMapping
  public ApiResponse<StoreCreateResponseDto> createStore(@RequestBody StoreCreateRequestDto storeCreateRequestDto){
    return ApiResponse.created(storeService.createStore(storeCreateRequestDto));
  }

  @PutMapping("{storeId}")
  public ApiResponse<StoreUpdateResponseDto> updateStore(
      @PathVariable("storeId") UUID storeId,
      @RequestBody StoreUpdateRequestDto storeUpdateRequestDto
  ){
    return ApiResponse.ok(storeService.updateStore(storeId, storeUpdateRequestDto));
  }

  @DeleteMapping("{storeId}")
  public ApiResponse deleteStore(@PathVariable("storeId") UUID storeId){
    storeService.deleteStore(storeId);
    return ApiResponse.ok(null);
  }

}
