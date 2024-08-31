package com.baedalping.delivery.global.utils;

import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));

    return UserDetailsImpl.builder()
        .userId(user.getUserId())
        .username(user.getUsername())
        .email(user.getEmail())
        .userRole(user.getRole())
        .isPublic(user.isPublic())
        .build();
  }
}
