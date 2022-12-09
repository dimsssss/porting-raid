package com.dimsssss.raid.raid.presentation.dto;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class RaidStartResponseDto {
    private Long raidRecordId;
    private boolean canEnter;
}
