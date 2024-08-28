package com.baedalping.delivery.domain.user.dto;

import com.baedalping.delivery.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateResponseDto {
  private Long userId;
  private String userName;
  private String email;
  private String userRole;
  private boolean isPublic;
  private LocalDateTime updatedAt;
  private String updatedBy;

  @Builder
  private UserUpdateResponseDto(
      Long userId,
      String userName,
      String email,
      String userRole,
      boolean isPublic,
      LocalDateTime updatedAt,
      String updatedBy) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.userRole = userRole;
    this.isPublic = isPublic;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }

  public static UserUpdateResponseDto ofEntity(User user) {
    return UserUpdateResponseDto.builder()
        .userId(user.getUserId())
        .userName(user.getUsername())
        .email(user.getEmail())
        .userRole(user.getRole().getRoleName())
        .isPublic(user.isPublic())
        .updatedAt(user.getUpdatedAt())
        .updatedBy(user.getUpdatedBy())
        .build();
  }
}
