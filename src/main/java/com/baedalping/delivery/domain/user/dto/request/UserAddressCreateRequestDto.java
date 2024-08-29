package com.baedalping.delivery.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAddressCreateRequestDto(
    @NotBlank(message = "주소는 필수값 입니다") String address,
    @NotBlank(message = "우편번호는 필수값 입니다")
        @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다")
        String zipcode,
    String alias) {}
