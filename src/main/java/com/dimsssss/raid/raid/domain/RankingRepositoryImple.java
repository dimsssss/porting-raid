package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.presentation.dto.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImple {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String KEY = "leaderboard";

    public int getPrevScore(Long userId) {
        Set<ZSetOperations.TypedTuple<String>> tuples = getAllScores();

        for (ZSetOperations.TypedTuple<String> stringTypedTuple: tuples) {
            if (parseLong(stringTypedTuple.getValue()) == userId) {
                return stringTypedTuple.getScore().intValue();
            }
        }
        return 0;
    }

    public void save(RaidRecordEntity raidRecordEntity) {
        int prevScore = getPrevScore(raidRecordEntity.getUserId());
        int totalScore = prevScore + raidRecordEntity.getScore();
        String userId = raidRecordEntity.getUserId().toString();
        stringRedisTemplate.opsForZSet().addIfAbsent(KEY, userId, totalScore);
    }

    private Set<ZSetOperations.TypedTuple<String>> getAllScores() {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, -1);
    }

    private List<RankingEntity> getRankAtAllScores(Set<ZSetOperations.TypedTuple<String>> scores) {
        int rank = 0;
        Iterator iterator = scores.iterator();
        List<RankingEntity> rankers = new ArrayList<>();

        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> stringTypedTuple = (ZSetOperations.TypedTuple<String>) iterator.next();
            RankingEntity rankingEntity = RankingEntity.builder()
                    .ranking(rank)
                    .userId(parseLong(stringTypedTuple.getValue()))
                    .totalScore(stringTypedTuple.getScore().intValue())
                    .build();
            rankers.add(rankingEntity);
            rank += 1;
        }
        return rankers;
    }

    private RankingEntity getMyRank(List<RankingEntity> rankers, Long userId) {
        for (RankingEntity rank: rankers) {
            if (rank.getUserId() == userId) {
                return rank;
            }
        }
        return null;
    }

    public RankingResponseDto getTopTankingAndMyRanking(Long userId) {
        Set<ZSetOperations.TypedTuple<String>> scores = getAllScores();
        RankingResponseDto rankingResponseDto = new RankingResponseDto();
        List<RankingEntity> rankers = getRankAtAllScores(scores);
        RankingEntity myRank = getMyRank(rankers, userId);
        rankingResponseDto.setTopRankerInfoList(rankers);
        rankingResponseDto.setMyRankingInfo(myRank);
        return rankingResponseDto;
    }
}
