package com.dimsssss.raid.raid.presentation.dto;

import com.dimsssss.raid.raid.domain.RaidRecordEntity;
import lombok.Getter;

@Getter
public class RaidStartResponseDto {
    private Long raidRecordId;
    private boolean canEnter;

    public RaidStartResponseDto(RaidRecordEntity raidRecordEntity) {
        this.raidRecordId = raidRecordEntity.getRaidRecordId();
        this.canEnter = true;
    }
}
