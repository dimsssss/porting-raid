package com.dimsssss.raid.raid.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTemplateTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeAll
    void setup() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @AfterAll
    void clean() {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    public void save() {
        boolean isCreated = stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "kk", 10.0);
        assertThat(isCreated).isTrue();

        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().reverseRangeWithScores("leaderboard", 0, -1);
    }
}
