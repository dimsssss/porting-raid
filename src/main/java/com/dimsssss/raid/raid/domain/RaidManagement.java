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

    public void validateRaidEnter(BossStateEntity bossStateEntity, LocalDateTime current) throws RaidTimeoutException {
        if (!isTimeOut(current, bossStateEntity)) {
            throw new RaidTimeoutException("현재 레이드가 진행중입니다");
        }
        if (!isTimeOut(current, bossStateEntity) && isRaiding(bossStateEntity)) {
            throw new RaidTimeoutException("현재 레이드가 진행중입니다");
        }
    }

    public void validateRaidEnd(BossStateEntity bossStateEntity, LocalDateTime current, Long userId) throws RaidTimeoutException {
        if (isTimeOut(current, bossStateEntity)) {
            throw new RaidTimeoutException("주어진 레이드 시간이 지났습니다");
        }
        if (!bossStateEntity.getLatestRaidUserId().equals(userId)) {
            throw new IllegalArgumentException("레이드 진행중인 아이디와 종료 아이디가 다릅니다. raidUserId: %s, requestUserId: %s");
        }
    }

    public void enterRaid(BossStateEntity bossStateEntity, LocalDateTime current) {
        bossStateEntity.onRaid();
        bossStateEntity.setRaidStartAt(current);
    }

    public void endRaid(BossStateEntity bossStateEntity) {
        bossStateEntity.offRaid();
    }
}
