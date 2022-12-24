package com.dimsssss.raid.raid.domain.dto;

import com.dimsssss.raid.raid.domain.RankingEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class RankingResponseDto {
    private RankingEntity myRankingInfo;
    private List<RankingEntity> topRankerInfoList;

    public RankingResponseDto(RankingEntity myRankingInfo, List<RankingEntity> topRankerInfoList) {
        this.myRankingInfo = myRankingInfo;
        this.topRankerInfoList = topRankerInfoList;
    }
}
