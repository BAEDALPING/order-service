package com.baedalping.delivery.domain.auth;

import com.baedalping.delivery.domain.auth.dto.AuthLoginRequestDto;
import com.baedalping.delivery.domain.auth.dto.AuthLoginResponseDto;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
  public final AuthService authService;

  @PostMapping
  public ApiResponse<AuthLoginResponseDto> login(
      @RequestBody @Validated AuthLoginRequestDto requestDto) {
    {
      return ApiResponse.created(authService.login(requestDto.email(), requestDto.password()));
    }
  }
}
