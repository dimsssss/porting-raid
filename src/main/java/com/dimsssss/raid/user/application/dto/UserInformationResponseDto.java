package com.dimsssss.raid.user.application.dto;

import com.dimsssss.raid.user.domain.RaidHistory;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInformationResponseDto {
    private int totalScore;
    private List<RaidHistory> bossRaidHistory;

    public UserInformationResponseDto(int totalScore, List<RaidHistory> bossRaidHistory) {
        this.totalScore = totalScore;
        this.bossRaidHistory = bossRaidHistory;
    }
}
