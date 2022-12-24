package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.domain.dto.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImple {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String KEY = "leaderboard";

    public Optional<Integer> getPrevScore(Long userId) {
        Set<ZSetOperations.TypedTuple<String>> tuples = getAllScores();

        for (ZSetOperations.TypedTuple<String> stringTypedTuple: tuples) {
            if (parseLong(stringTypedTuple.getValue()) == userId) {
                return Optional.ofNullable(stringTypedTuple.getScore().intValue());
            }
        }
        return Optional.empty();
    }

    public void save(RaidRecordEntity raidRecordEntity) {
        Optional<Integer> prevScore = getPrevScore(raidRecordEntity.getUserId());
        int totalScore = 0;

        if (prevScore.isPresent()) {
            totalScore = prevScore.get() + raidRecordEntity.getScore();
        }

        String userId = raidRecordEntity.getUserId().toString();
        stringRedisTemplate.opsForZSet().addIfAbsent(KEY, userId, totalScore);
    }

    private Set<ZSetOperations.TypedTuple<String>> findAll() {
        int start = 0;
        int end = -1;
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(KEY, start, end);
    }

    private RankingEntity convertFrom(ZSetOperations.TypedTuple<String> score, int ranking) {
        int totalScore = score.getScore().intValue();
        Long userId = parseLong(score.getValue());
        return RankingEntity.builder()
                .totalScore(totalScore)
                .userId(userId)
                .ranking(ranking)
                .build();
    }

    private List<RankingEntity> getRankingEntity(Set<ZSetOperations.TypedTuple<String>> scores) {
        List<RankingEntity> rankings = new ArrayList<>();
        int ranking = 0;

        for (ZSetOperations.TypedTuple<String> score: scores) {
            RankingEntity rank = convertFrom(score, ranking);
            ranking += 1;
            rankings.add(rank);
        }
        return rankings;
    }

    private List<RankingEntity> getAllRankWithScores(Set<ZSetOperations.TypedTuple<String>> scores) {
        return getRankingEntity(scores);
    }

    private RankingEntity getMyRank(List<RankingEntity> rankers, Long userId) {
        return rankers.stream()
                .filter(ranker -> ranker.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public RankingResponseDto getTopRankingAndMyRanking(Long userId) {
        Set<ZSetOperations.TypedTuple<String>> scores = findAll();
        List<RankingEntity> rankers = getAllRankWithScores(scores);
        RankingEntity myRank = getMyRank(rankers, userId);
        return new RankingResponseDto(myRank, rankers);
    }
}
