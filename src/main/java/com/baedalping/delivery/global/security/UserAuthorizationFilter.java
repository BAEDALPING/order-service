package com.baedalping.delivery.global.security;

import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.global.utils.JwtTokenUtils;
import com.baedalping.delivery.global.utils.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j(topic = "UserAuthorizationFilter")
public class UserAuthorizationFilter extends OncePerRequestFilter {
  private final JwtTokenUtils utils;
  private final UserDetailServiceImpl userDetailService;
  private static final List<String> PERMIT_URLS = List.of("/", "/auth/login");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return PERMIT_URLS.stream()
        .anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String email = request.getHeader("requestedUserEmail");
      log.info("requested User email: {}", email);
      if (email == null) throw new DeliveryApplicationException(ErrorCode.INVALID_TOKEN);

      UserDetails userDetails = userDetailService.loadUserByUsername(email);

      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      log.info("User Authorization successful. URI: {}", request.getRequestURI());

    } catch (RuntimeException exception) {
      log.error("occurs exception in UserAuthorizationFilter");
      request.setAttribute("exception", exception);
    }

    filterChain.doFilter(request, response);
  }
}
