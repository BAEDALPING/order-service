package com.baedalping.delivery.domain.user.controller;

import com.baedalping.delivery.domain.user.dto.UserCreateRequestDto;
import com.baedalping.delivery.domain.user.dto.UserCreateResponseDto;
import com.baedalping.delivery.domain.user.dto.UserReadResponseDto;
import com.baedalping.delivery.domain.user.dto.UserUpdateRequestDto;
import com.baedalping.delivery.domain.user.dto.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @PostMapping
  public ApiResponse<UserCreateResponseDto> create(
      @RequestBody @Validated UserCreateRequestDto requestDto) {
    return ApiResponse.created(
        userService.create(requestDto.username(), requestDto.password(), requestDto.email()));
  }

  @PutMapping
  public ApiResponse<UserUpdateResponseDto> update(
      @RequestBody @Validated UserUpdateRequestDto requestDto) {
    Long userId = 1L; // TODO :: spring security 적용 이후 principle로 userId 검증 추가
    return ApiResponse.ok(
        userService.updateUserInfo(
            userId, requestDto.username(), requestDto.password(), requestDto.email()));
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<UserReadResponseDto> delete(@PathVariable("userId") Long userId){
    // TODO :: spring security 적용 이후 principle에 있는 userId를 가져올 예정
    return ApiResponse.ok(userService.delete(userId));
  }
}
