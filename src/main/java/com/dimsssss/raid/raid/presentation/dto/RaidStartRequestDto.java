package com.dimsssss.raid.raid.presentation.dto;

import com.dimsssss.raid.raid.domain.RaidRecordEntity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class RaidStartRequestDto {
    private Long userId;
    private int level;
    private LocalDateTime raidStartTime;

    public RaidStartRequestDto(Long userId, int level, LocalDateTime raidStartTime) {
        this.userId = userId;
        this.level = level;
        this.raidStartTime = raidStartTime;
    }
    public RaidRecordEntity convertFrom() {
        return RaidRecordEntity.builder()
                .userId(this.userId)
                .score(this.level)
                .build();
    }
}
