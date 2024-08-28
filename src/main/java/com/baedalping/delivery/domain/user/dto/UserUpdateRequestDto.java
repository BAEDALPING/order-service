package com.baedalping.delivery.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequestDto(
    @NotBlank(message = "유저이름은 필수값 입니다")
        @Pattern(
            regexp = "^[a-z0-9]{4,10}$",
            message = "유저이름은 영어소문자, 숫자로 이루어진 4자이상 8자 이하의 문자열이어야 합니다")
        String username,
    @NotBlank(message = "패스워드는 필수값 입니다")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&*+=!]).{8,15}$",
            message = "패스워드는 영어소문자, 숫자, 특수문자로 이루어진 8자 이상 15자 이하의 문자열이어야 합니다")
        String password,
    @NotBlank(message = "유저이메일은 필수값 입니다") @Email(message = "이메일 형식에 맞지 않습니다") String email) {}
