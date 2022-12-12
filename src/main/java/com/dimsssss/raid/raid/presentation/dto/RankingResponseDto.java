package com.dimsssss.raid.raid.presentation.dto;

import com.dimsssss.raid.raid.domain.RankingEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RankingResponseDto {
    private RankingEntity myRankingInfo;
    private List<RankingEntity> topRankerInfoList;
}
