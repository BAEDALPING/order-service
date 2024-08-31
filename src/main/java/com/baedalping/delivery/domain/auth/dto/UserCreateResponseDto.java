package com.baedalping.delivery.domain.auth.dto;

import com.baedalping.delivery.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateResponseDto {
  private Long userId;
  private String userName;
  private String email;
  private String userRole;
  private boolean isPublic;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;

  @Builder
  private UserCreateResponseDto(
      Long userId,
      String userName,
      String email,
      String userRole,
      boolean isPublic,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.userRole = userRole;
    this.isPublic = isPublic;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static UserCreateResponseDto ofEntity(User user) {
    return UserCreateResponseDto.builder()
        .userId(user.getUserId())
        .userName(user.getUsername())
        .email(user.getEmail())
        .userRole(user.getRole().getRoleName())
        .isPublic(user.isPublic())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .deletedAt(user.getDeletedAt())
        .build();
  }
}
