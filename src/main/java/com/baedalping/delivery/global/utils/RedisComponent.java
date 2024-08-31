package com.baedalping.delivery.global.utils;

import com.baedalping.delivery.domain.user.dto.response.UserAuthorityResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "RedisService")
public class RedisComponent {
  @Cacheable(cacheNames = "roleCache", key = "args[0]")
  public UserAuthorityResponseDto setUserAuthorityInRedis(String email, String role) {
    return new UserAuthorityResponseDto(email, role);
  }
}
