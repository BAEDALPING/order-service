package com.baedalping.delivery.global.utils;

import com.baedalping.delivery.domain.user.entity.UserRole;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtTokenUtils {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  @Value("${jwt.secret-key}")
  private String secret;

  @Value("${jwt.access-time}")
  private Long accessExpiredTimeMs;

  public String generateAccessToken(String email, UserRole role) {
    Claims claims = Jwts.claims();
    claims.put("email", email);
    claims.put("role", role.getRoleName());

    return BEARER_PREFIX
        + Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(getKey(secret), SignatureAlgorithm.HS256)
            .compact();
  }

  public String resolveToken(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
      return token.split(" ")[1].trim();
    }
    throw new DeliveryApplicationException(ErrorCode.INVALID_TOKEN);
  }

  public String getUserSignature(String token) {
    try {
      Claims claims =
          Jwts.parserBuilder()
              .setSigningKey(getKey(secret))
              .build()
              .parseClaimsJws(token)
              .getBody();
      return claims.get("email", String.class);

    } catch (ExpiredJwtException e) {
      throw new DeliveryApplicationException(ErrorCode.EXPIRED_TOKEN);
    } catch (SecurityException
        | MalformedJwtException
        | SignatureException
        | UnsupportedJwtException
        | IllegalArgumentException e) {
      throw new DeliveryApplicationException(ErrorCode.INVALID_TOKEN);
    }
  }

  public static Key getKey(String secretKey) {
    byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyByte);
  }
}
