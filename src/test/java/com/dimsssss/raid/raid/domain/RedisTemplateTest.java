package com.dimsssss.raid.raid.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTemplateTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeAll
    void setup() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @AfterAll
    void clean() {
        redisTemplate.delete("how");
    }

    @Test
    public void save() {
        boolean isCreated = redisTemplate.opsForZSet().addIfAbsent("how", "hohoho", 1.0);
        assertThat(isCreated).isTrue();
    }
}
