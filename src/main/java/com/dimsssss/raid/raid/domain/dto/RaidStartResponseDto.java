package com.dimsssss.raid.raid.domain.dto;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class RaidStartResponseDto {
    private Long raidRecordId;
    private boolean canEnter;
}
