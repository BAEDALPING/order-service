package com.baedalping.delivery.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserAuthorityResponseDto {
  private String email;
  private String role;
}
