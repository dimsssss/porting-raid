package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.presentation.dto.BossStateResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "boss_state")
public class BossStateEntity {
    @GeneratedValue
    @Id
    private Long bossStateId;
    @Column(columnDefinition = "boolean default false")
    private boolean isRaiding;
    @Column(columnDefinition = "integer default 3")
    private int raidingMinute = 3;
    @Column
    private Long latestRaidUserId;
    @Column
    private LocalDateTime raidStartAt;
    @Column
    private LocalDateTime raidEndAt;
    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime deletedAt;

    @Version
    private Timestamp timestamp;

    @Builder
    public BossStateEntity() {}

    public void onRaid() {
        isRaiding = true;
    }

    public void offRaid() {
        isRaiding = false;
    }

    public void setRaidStartAt(LocalDateTime raidStartAt) {
        this.raidStartAt = raidStartAt;
    }

    public void setLatestRaidUserId(Long userId) {
        this.latestRaidUserId = userId;
    }

    private boolean isTimeOut(LocalDateTime current) {
        LocalDateTime latestStartRaidDateTime = this.getRaidStartAt();

        if (latestStartRaidDateTime == null) {
            return true;
        }

        int raidTime = this.getRaidingMinute();
        return current.isAfter(latestStartRaidDateTime.plusMinutes(raidTime));
    }

    public void enterRaid(LocalDateTime current) {
        this.onRaid();
        this.setRaidStartAt(current);
    }

    public void validateRaidEnter(LocalDateTime current) throws RaidTimeoutException {
        if (!isTimeOut(current)) {
            throw new RaidTimeoutException("현재 레이드가 진행중입니다");
        }
        if (!isTimeOut(current) && this.isRaiding()) {
            throw new RaidTimeoutException("현재 레이드가 진행중입니다");
        }
    }
    public void validateRaidEnd(LocalDateTime current, Long userId) throws RaidTimeoutException {
        if (isTimeOut(current)) {
            throw new RaidTimeoutException("주어진 레이드 시간이 지났습니다");
        }
        if (!this.getLatestRaidUserId().equals(userId)) {
            throw new IllegalArgumentException("레이드 진행중인 아이디와 종료 아이디가 다릅니다. raidUserId: %s, requestUserId: %s");
        }
    }

    public BossStateResponseDto toBossStateResponseDto(boolean canEnter) {
        return BossStateResponseDto.builder()
                .canEnter(canEnter)
                .enteredUserId(this.latestRaidUserId)
                .build();
    }
}
