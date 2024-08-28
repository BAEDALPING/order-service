package com.baedalping.delivery.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.baedalping.delivery.domain.user.dto.UserReadResponseDto;
import com.baedalping.delivery.domain.user.dto.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

  private String username;
  private String password;
  private String email;
  private Long userId;
  private User mockUser;

  @BeforeEach
  void setUp() {
    username = "username";
    password = "pass123***";
    email = "test@test.com";
    userId = 1L;
    mockUser = UserFixture.get(username, password, email);
  }

  @Test
  void 이메일이_중복되어_회원가입에_실패한다() {
    when(userRepository.existsByEmail(email)).thenReturn(Boolean.TRUE);
    DeliveryApplicationException exception =
        assertThrows(
            DeliveryApplicationException.class,
            () -> userService.create(username, password, email));

    assertEquals(exception.getErrorCode().getStatus(), HttpStatus.CONFLICT);
    assertEquals(exception.getErrorCode().getMessage(), "이미 가입된 유저 이메일 입니다");
  }

  @Test
  void 유저정보_업데이트에_성공한다() {
    username = "updatename";
    password = "newpass123@@";

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));
    UserUpdateResponseDto response = userService.updateUserInfo(userId, username, password, email);

    assertThat(response.getUserName()).isEqualTo(username);
  }

  @Test
  void 유저정보_업데이트시_유저정보를_찾을수없어_실패한다() {
    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
    DeliveryApplicationException exception =
        assertThrows(
            DeliveryApplicationException.class,
            () -> userService.updateUserInfo(userId, username, password, email));

    assertEquals(exception.getErrorCode().getStatus(), HttpStatus.NOT_FOUND);
    assertEquals(exception.getErrorCode().getMessage(), "가입된 유저가 아닙니다");
  }

  @Test
  void 유저탈퇴시_퍼블릭여부를_트루값으로_변경한다() {
    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));
    UserReadResponseDto response = userService.delete(userId);

    assertThat(response.isPublic()).isFalse();
  }
}
