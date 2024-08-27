package com.baedalping.delivery.user;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.baedalping.delivery.domain.user.controller.UserController;
import com.baedalping.delivery.domain.user.dto.UserCreateRequestDto;
import com.baedalping.delivery.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  @Test
  @WithMockUser
  public void 받아온_유저정보로_회원가입에_성공한다() throws Exception {
    String username = "username";
    String password = "password123**";
    String email = "email@test.com";

    UserCreateRequestDto requestDto = new UserCreateRequestDto(username, password, email);

    mockMvc
        .perform(
            post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser
  public void 입력값_검증에_실패하여_회원가입에_실패한다() throws Exception {
    String username = ""; // 공백, 유저네임 정책에 맞지 않음
    String password = "password"; // 패스워드 정책에 맞지 않음
    String email = "emailtest.com"; // 이메일 형식에 맞지 않음

    UserCreateRequestDto requestDto = new UserCreateRequestDto(username, password, email);

    mockMvc
        .perform(
            post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", hasSize(4)))
        .andDo(print());
  }
}
