package com.baedalping.delivery.domain.user.dto.request;

import com.baedalping.delivery.domain.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequestDto(
    @NotNull(message = "변경할 유저의 아이디는 필수값 입니다") Long userId,
    @NotNull(message = "변경할 유저역할은 필수값 입니다") UserRole role
) {}
