package com.baedalping.delivery.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDto(
    @NotBlank(message = "이메일은 필수값 입니다") @Email(message = "이메일 형식이 맞지 않습니다") String email,
    @NotBlank(message = "비밀번호는 필수값 입니다") String password) {}
