package com.baedalping.delivery.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.baedalping.delivery.domain.user.dto.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private BCryptPasswordEncoder encoder;
  @InjectMocks private UserService userService;

  @Test
  void 이메일이_중복되어_회원가입에_실패한다() {
    String username = "username";
    String password = "pass123***";
    String duplicatedEmail = "test@test.com";

    when(userRepository.existsByEmail(duplicatedEmail)).thenReturn(Boolean.TRUE);
    DeliveryApplicationException exception =
        assertThrows(
            DeliveryApplicationException.class,
            () -> userService.create(username, password, duplicatedEmail));

    assertEquals(exception.getErrorCode().getStatus(), HttpStatus.CONFLICT);
    assertEquals(exception.getErrorCode().getMessage(), "이미 가입된 유저 이메일 입니다");
  }

  @Test
  void 유저정보_업데이트에_성공한다() {
    Long userId = 1L;
    String username = "updatename";
    String password = "newpass123@@";
    String email = "test@test.com";
    User user = User.builder().email(email).password(password).email(email).build();
    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    UserUpdateResponseDto response = userService.updateUserInfo(userId, username, password, email);
    assertThat(response.getUserName()).isEqualTo(username);
  }

  @Test
  void 유저정보_업데이트시_유저정보를_찾을수없어_실패한다() {
    Long userId = 1L;
    String username = "username";
    String password = "pass123***";
    String email = "test@test.com";

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
    DeliveryApplicationException exception =
        assertThrows(
            DeliveryApplicationException.class,
            () -> userService.updateUserInfo(userId, username, password, email));
    assertEquals(exception.getErrorCode().getStatus(), HttpStatus.NOT_FOUND);
    assertEquals(exception.getErrorCode().getMessage(), "가입된 유저가 아닙니다");
  }
}
