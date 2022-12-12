package com.dimsssss.raid.raid.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingEntity {
    private int ranking;
    private Long userId;
    private int totalScore;


    public void setTotalScore(int score) {
        this.totalScore = score;
    }

    public void accumulateTotalScore(int score) {
        this.totalScore += score;
    }
}
