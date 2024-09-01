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

  @GetMapping
  public ApiResponse<UserReadResponseDto> get(@AuthenticationPrincipal UserDetailsImpl userDto) {
    return ApiResponse.ok(userService.get(userDto.getUserId()));
  }

  @PutMapping
  public ApiResponse<UserUpdateResponseDto> update(
      @AuthenticationPrincipal UserDetailsImpl userDto,
      @RequestBody @Validated UserUpdateRequestDto requestDto) {
    return ApiResponse.ok(
        userService.updateUserInfo(
            userDto.getUserId(), requestDto.username(), requestDto.password(), requestDto.email()));
  }

  @DeleteMapping
  public ApiResponse<UserReadResponseDto> delete(@AuthenticationPrincipal UserDetailsImpl userDto) {
    return ApiResponse.ok(userService.delete(userDto.getUserId()));
  }

  @PostMapping("/address")
  public ApiResponse<UserAddressResponseDto> createAddress(
      @AuthenticationPrincipal UserDetailsImpl userDto,
      @RequestBody @Validated UserAddressCreateRequestDto requestDto) {
    return ApiResponse.created(
        userService.addAddress(
            userDto.getUserId(), requestDto.address(), requestDto.zipcode(), requestDto.alias()));
  }

  @GetMapping("/address")
  public ApiResponse<List<UserAddressResponseDto>> getAddressList(
      @AuthenticationPrincipal UserDetailsImpl userDto) {
    return ApiResponse.ok(userService.getAddressList(userDto.getUserId()));
  }

  @PutMapping("/address/{addressId}")
  public ApiResponse<UserAddressResponseDto> updateAddress(
      @PathVariable("addressId") UUID addressId,
      @AuthenticationPrincipal UserDetailsImpl userDto,
      @RequestBody @Validated UserAddressUpdateRequestDto requestDto) {
    return ApiResponse.ok(
        userService.updateAddress(
            addressId,
            userDto.getUserId(),
            requestDto.address(),
            requestDto.zipcode(),
            requestDto.alias()));
  }
}
