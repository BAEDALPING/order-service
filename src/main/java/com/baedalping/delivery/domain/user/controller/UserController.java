package com.baedalping.delivery.domain.user.controller;

import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.domain.user.dto.request.UserAddressCreateRequestDto;
import com.baedalping.delivery.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.baedalping.delivery.domain.user.dto.request.UserUpdateRequestDto;
import com.baedalping.delivery.domain.user.dto.response.UserAddressResponseDto;
import com.baedalping.delivery.domain.user.dto.response.UserReadResponseDto;
import com.baedalping.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{userId}")
  public ApiResponse<UserReadResponseDto> get(@PathVariable("userId") Long userId) {
    // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
    return ApiResponse.ok(userService.get(userId));
  }

  @PutMapping
  public ApiResponse<UserUpdateResponseDto> update(
      @AuthenticationPrincipal UserDetailsImpl userDto,
      @RequestBody @Validated UserUpdateRequestDto requestDto) {
    return ApiResponse.ok(
        userService.updateUserInfo(
            userDto.getUserId(), requestDto.username(), requestDto.password(), requestDto.email()));
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<UserReadResponseDto> delete(@PathVariable("userId") Long userId) {
    // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
    return ApiResponse.ok(userService.delete(userId));
  }

  @PostMapping("/address/{userId}")
  public ApiResponse<UserAddressResponseDto> createAddress(
      @PathVariable("userId") Long userId,
      @RequestBody @Validated UserAddressCreateRequestDto requestDto) {
    // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
    return ApiResponse.created(
        userService.addAddress(
            userId, requestDto.address(), requestDto.zipcode(), requestDto.alias()));
  }

  @GetMapping("/address/{userId}")
  public ApiResponse<List<UserAddressResponseDto>> getAddressList(
      @PathVariable("userId") Long userId) {
    // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
    return ApiResponse.ok(userService.getAddressList(userId));
  }

  @PutMapping("/address/{addressId}/{userId}")
  public ApiResponse<UserAddressResponseDto> updateAddress(
      // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
      @PathVariable("addressId") UUID addressId,
      @PathVariable("userId") Long userId,
      @RequestBody @Validated UserAddressUpdateRequestDto requestDto) {
    return ApiResponse.ok(
        userService.updateAddress(
            addressId, userId, requestDto.address(), requestDto.zipcode(), requestDto.alias()));
  }
}
