package com.baedalping.delivery.global.security;

import static com.baedalping.delivery.global.utils.JwtTokenUtils.AUTHORIZATION_HEADER;

import com.baedalping.delivery.domain.auth.dto.AuthLoginRequestDto;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.global.common.ApiResponse;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.global.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "Login")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final JwtTokenUtils utils;
  private final String LOGIN_URI = "/auth/login";

  public JwtAuthenticationFilter(JwtTokenUtils utils) {
    this.utils = utils;
    setFilterProcessesUrl(LOGIN_URI);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      AuthLoginRequestDto requestDto =
          new ObjectMapper().readValue(request.getInputStream(), AuthLoginRequestDto.class);

      return getAuthenticationManager()
          .authenticate(
              new UsernamePasswordAuthenticationToken(
                  requestDto.email(), requestDto.password(), null));

    } catch (IOException e) {
      log.error("Occurs in JwtAuthenticationFilter");
      throw new DeliveryApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth) {
    UserDetailsImpl details = (UserDetailsImpl) (auth.getPrincipal());
    String accessToken = utils.generateAccessToken(details.getEmail(), details.getUserRole());
    response.addHeader(AUTHORIZATION_HEADER, accessToken);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(ErrorCode.INVALID_PASSWORD.getStatus().value());

    String responseData = new ObjectMapper().writeValueAsString(ApiResponse.error(ErrorCode.INVALID_PASSWORD));
    response.getWriter().write(responseData);
  }
}
