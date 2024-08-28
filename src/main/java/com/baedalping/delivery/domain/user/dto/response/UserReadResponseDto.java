package com.baedalping.delivery.domain.user.dto.response;

import com.baedalping.delivery.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserReadResponseDto{
  private Long userId;
  private String userName;
  private String email;
  private String userRole;
  private boolean isPublic;
  private LocalDateTime createdAt;
  private String createBy;
  private LocalDateTime updatedAt;
  private String updatedBy;
  private LocalDateTime deletedAt;
  private String deletedBy;

  @Builder
  private UserReadResponseDto(
      Long userId,
      String userName,
      String email,
      String userRole,
      boolean isPublic,
      LocalDateTime createdAt,
      String createBy,
      LocalDateTime updatedAt,
      String updatedBy,
      LocalDateTime deletedAt,
      String deletedBy) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.userRole = userRole;
    this.isPublic = isPublic;
    this.createdAt = createdAt;
    this.createBy = createBy;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
    this.deletedAt = deletedAt;
    this.deletedBy = deletedBy;
  }

  public static UserReadResponseDto ofEntity(User user) {
    return UserReadResponseDto.builder()
        .userId(user.getUserId())
        .userName(user.getUsername())
        .email(user.getEmail())
        .userRole(user.getRole().getRoleName())
        .isPublic(user.isPublic())
        .createdAt(user.getCreatedAt())
        .createBy(user.getCreatedBy())
        .updatedAt(user.getUpdatedAt())
        .updatedBy(user.getUpdatedBy())
        .deletedAt(user.getDeletedAt())
        .deletedBy(user.getDeletedBy())
        .build();
  }
}
