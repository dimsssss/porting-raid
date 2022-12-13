package com.dimsssss.raid.raid.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BossStateResponseDto {
    private boolean canEnter;
    private Long enteredUserId;
}
