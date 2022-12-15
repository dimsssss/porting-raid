package com.dimsssss.raid.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RaidHistory {
    private Long raidRecordId;
    private int score;
    private LocalDateTime enterTime;
    private LocalDateTime endTime;
}
