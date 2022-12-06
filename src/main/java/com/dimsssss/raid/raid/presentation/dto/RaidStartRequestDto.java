package com.dimsssss.raid.raid.presentation.dto;

import com.dimsssss.raid.raid.domain.RaidRecordEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RaidStartRequestDto {
    private Long userId;
    private int level;

    public RaidStartRequestDto(Long userId, int level) {
        this.userId = userId;
        this.level = level;
    }
    @Builder
    public RaidRecordEntity convertFrom() {
        return RaidRecordEntity.builder()
                .userId(this.userId)
                .score(this.level)
                .build();
    }
}
