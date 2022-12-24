package com.dimsssss.raid.raid.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RaidEndRequestDto {
    private Long userId;
    private Long raidRecordId;
    private Long bossStateId;
}
