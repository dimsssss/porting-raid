package com.dimsssss.raid.raid.domain;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RaidManagement {
    private boolean isRaiding(BossStateEntity bossStateEntity) {
        return bossStateEntity.isRaiding();
    }

    private boolean isTimeOut(LocalDateTime current, BossStateEntity bossStateEntity) {
        LocalDateTime latestStartRaidDateTime = bossStateEntity.getRaidStartAt();

        if (latestStartRaidDateTime == null) {
            return true;
        }

        int raidTime = bossStateEntity.getRaidingMinute();
        return current.isAfter(latestStartRaidDateTime.plusMinutes(raidTime));
    }

    public void validateRaidEnter(BossStateEntity bossStateEntity, LocalDateTime current) throws NotCantRaidException {
        if (!isTimeOut(current, bossStateEntity)) {
            throw new NotCantRaidException("현재 레이드가 진행중입니다");
        }
        if (!isTimeOut(current, bossStateEntity) && isRaiding(bossStateEntity)) {
            throw new NotCantRaidException("현재 레이드가 진행중입니다");
        }
    }

    public void enterRaid(BossStateEntity bossStateEntity, LocalDateTime current) {
        bossStateEntity.onRaid();
        bossStateEntity.setRaidStartAt(current);
    }
}
