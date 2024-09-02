package com.baedalping.delivery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        // 키-값 설정
        String key = "testKey";
        String value = "testValue";

        // Redis에 데이터 저장
        redisTemplate.opsForValue().set(key, value);

        // Redis에서 데이터 가져오기
        Object retrievedValue = redisTemplate.opsForValue().get(key);

        // 데이터 검증
        assertThat(retrievedValue).isEqualTo(value);
    }
}

