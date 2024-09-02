package com.baedalping.delivery.domain.user.service;

import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.service.StoreService;
import com.baedalping.delivery.domain.user.dto.response.UserAuthorityResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.entity.UserRole;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;
  private final StoreService storeService;

  @Transactional
  @CacheEvict(cacheNames = "roleCache", key = "#user.email")
  public UserAuthorityResponseDto updateUserRole(Long userId, UserRole role) {
    User user = findByUserId(userId);
    user.updateUserRole(role);
    userRepository.flush();
    return new UserAuthorityResponseDto(user.getEmail(), user.getRole().getRoleName());
  }

  private User findByUserId(Long userId) {
    return userRepository
        .findByUserId(userId)
        .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));
  }

  @Transactional
  public void permitStore(UUID storeId) {
    storeService.findById(storeId).setPublic(true);
  }
}
