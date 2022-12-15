package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.domain.dto.RankingResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTemplateTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RankingRepositoryImple rankingRepositoryImple;

    @BeforeAll
    void setup() {
        stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "1", 1);
        stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "2", 2);
        stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "3", 3);
        stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "4", 4);
        stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "5", 5);
    }

    @AfterAll
    void clean() {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    public void save() {
        boolean isCreated = stringRedisTemplate.opsForZSet().addIfAbsent("leaderboard", "6", 10.0);
        assertThat(isCreated).isTrue();
        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().reverseRangeWithScores("*", 0, -1);
    }

    @Test
    public void findAll() {
        RankingResponseDto rankingInfo = rankingRepositoryImple.getTopTankingAndMyRanking(3L);
        RankingEntity myRanking = rankingInfo.getMyRankingInfo();
        List<RankingEntity> topRanking = rankingInfo.getTopRankerInfoList();

        assertThat(myRanking.getRanking()).isEqualTo(2);
        assertThat(myRanking.getUserId()).isEqualTo(3);

        int score = 5;

        for (RankingEntity rankingEntity : topRanking) {
            assertThat(rankingEntity.getTotalScore()).isEqualTo(score);
            score -= 1;
        }

    }
}
