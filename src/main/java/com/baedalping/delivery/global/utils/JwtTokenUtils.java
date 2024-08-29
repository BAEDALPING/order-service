package com.baedalping.delivery.global.utils;
import com.baedalping.delivery.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.lettuce.core.models.stream.ClaimedMessages;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtTokenUtils {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  @Value("${jwt.secret-key}")
  private String secret;

  @Value("${jwt.access-time}")
  private Long accessExpiredTimeMs;

  public String generateAccessToken(Long userId, UserRole role){
    Claims claims = Jwts.claims();
    claims.put("userId", userId);
    claims.put("role", role.getRoleName());

    return BEARER_PREFIX + Jwts.builder()
        .setClaims(claims)
        .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .signWith(getKey(secret), SignatureAlgorithm.HS256)
        .compact();

  }
  public static Key getKey(String secretKey) {
    byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyByte);
  }

}
