package com.dimsssss.raid.raid.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BossStateResponseDto {
    private boolean canEnter;
    private Long enteredUserId;
}
