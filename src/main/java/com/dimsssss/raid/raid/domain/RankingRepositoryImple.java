package com.dimsssss.raid.raid.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImple {

    private final StringRedisTemplate stringRedisTemplate;

    public void save(RankingEntity rankingEntity) {
        String key = "leadreboard:" + rankingEntity.getUserId();
        stringRedisTemplate.opsForZSet().addIfAbsent(key, rankingEntity.getUserId().toString(), rankingEntity.getTotalScore());
    }

    public RankingEntity findById(Long userId) {
        String key = "leadreboard:" + userId;
        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().distinctRandomMembersWithScore(key, 1);
        return result.stream().map((ranking) -> RankingEntity.builder()
                .userId(parseLong(ranking.getValue()))
                .totalScore(ranking.getScore().intValue())
                .build()).findFirst().orElse(null);
    }
}
