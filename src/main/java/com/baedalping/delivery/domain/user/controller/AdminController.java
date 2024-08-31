package com.baedalping.delivery.domain.user.controller;

import com.baedalping.delivery.domain.user.dto.request.UserRoleUpdateRequestDto;
import com.baedalping.delivery.domain.user.dto.response.UserAuthorityResponseDto;
import com.baedalping.delivery.domain.user.service.AdminService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
  private final AdminService adminService;

  @PutMapping("/role")
  public ApiResponse<UserAuthorityResponseDto> updateUserRole(
      @RequestBody @Validated UserRoleUpdateRequestDto requestDto) {
    return ApiResponse.ok(adminService.updateUserRole(requestDto.userId(), requestDto.role()));
  }
}
