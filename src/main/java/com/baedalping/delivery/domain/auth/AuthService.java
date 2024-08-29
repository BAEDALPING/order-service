package com.baedalping.delivery.domain.auth;

import com.baedalping.delivery.domain.auth.dto.AuthLoginResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.global.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;
  private final JwtTokenUtils utils;

  @Transactional
  public AuthLoginResponseDto login(String email, String password){
    User savedUser = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));

    validatePassword(savedUser.getPassword(), password);
    String token = utils.generateAccessToken(savedUser.getUserId(), savedUser.getRole());

    return AuthLoginResponseDto.of(token);
  }
  private void validatePassword(String encoded, String password){
    if(!encoder.matches(encoded, password)){
      throw new DeliveryApplicationException(ErrorCode.INVALID_PASSWORD);
    }
  }
}
