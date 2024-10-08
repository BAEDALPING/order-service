package com.baedalping.delivery.domain.auth;

import com.baedalping.delivery.domain.auth.dto.UserCreateRequestDto;
import com.baedalping.delivery.domain.auth.dto.UserCreateResponseDto;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;
  @PostMapping
  public ApiResponse<UserCreateResponseDto> create(
      @RequestBody @Validated UserCreateRequestDto requestDto) {
    return ApiResponse.created(
        userService.create(requestDto.username(), requestDto.password(), requestDto.email()));
  }
}
