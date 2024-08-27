package com.baedalping.delivery.store;

import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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


}
