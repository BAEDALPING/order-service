package com.baedalping.delivery.domain.user.service;

import com.baedalping.delivery.domain.user.dto.UserCreateResponseDto;
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

  private void validateUniqueEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DeliveryApplicationException(ErrorCode.DUPLICATED_USER);
    }
  }
}
