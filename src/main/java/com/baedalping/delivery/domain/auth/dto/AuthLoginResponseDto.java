package com.baedalping.delivery.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponseDto {
  @NotBlank private String accessToken;

  public static AuthLoginResponseDto of(String token) {
    return new AuthLoginResponseDto(token);
  }
}
