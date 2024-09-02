package com.baedalping.delivery.domain.user.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserAuthorityResponseDto implements Serializable {
  private String email;
  private String role;
}
