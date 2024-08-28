package com.baedalping.delivery.domain.user.service;

import com.baedalping.delivery.domain.user.dto.UserCreateResponseDto;
import com.baedalping.delivery.domain.user.dto.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  @Transactional
  public UserCreateResponseDto create(String username, String password, String email) {
    validateUniqueEmail(email);
    User newUser =
        User.builder().username(username).password(encoder.encode(password)).email(email).build();
    return UserCreateResponseDto.ofEntity(userRepository.save(newUser));
  }

  @Transactional
  public UserUpdateResponseDto updateUserInfo(
      Long userId, String username, String password, String email) {
    User user =
        userRepository
            .findByUserId(userId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));

    user.updateInfo(username, encoder.encode(password), email);
    userRepository.saveAndFlush(user);

    return UserUpdateResponseDto.ofEntity(user);
  }

  private void validateUniqueEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DeliveryApplicationException(ErrorCode.DUPLICATED_USER);
    }
  }
}
