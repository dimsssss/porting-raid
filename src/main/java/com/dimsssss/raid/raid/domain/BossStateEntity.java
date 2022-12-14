package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.domain.dto.BossStateResponseDto;
import com.dimsssss.raid.raid.presentation.dto.RaidEndRequestDto;
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

    public BossStateEntity() {}

    private BossStateEntity(Long bossStateId, boolean isRaiding, int raidingMinute, Long latestRaidUserId, LocalDateTime raidStartAt,
                           LocalDateTime raidEndAt, LocalDateTime createdAt, LocalDateTime deletedAt, Timestamp timestamp) {
        this.bossStateId = bossStateId;
        this.isRaiding = isRaiding;
        this.raidingMinute = raidingMinute;
        this.latestRaidUserId = latestRaidUserId;
        this.raidStartAt = raidStartAt;
        this.raidEndAt = raidEndAt;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.timestamp = timestamp;
    }

    public BossStateEntity withRaidingState(boolean isRaiding) {
        return new BossStateEntity(
                bossStateId,
                isRaiding,
                raidingMinute,
                latestRaidUserId,
                raidStartAt,
                raidEndAt,
                createdAt,
                deletedAt,
                timestamp);
    }

    public BossStateEntity withRaidingStateAndStartTime(boolean isRaiding, LocalDateTime startTime) {
        return new BossStateEntity(
                bossStateId,
                isRaiding,
                raidingMinute,
                latestRaidUserId,
                startTime,
                raidEndAt,
                createdAt,
                deletedAt,
                timestamp);
    }

    public BossStateEntity withUserId(Long userId) {
        return new BossStateEntity(
                bossStateId,
                isRaiding,
                raidingMinute,
                userId,
                raidStartAt,
                raidEndAt,
                createdAt,
                deletedAt,
                timestamp);
    }

    private boolean isTimeOut(LocalDateTime current) {
        LocalDateTime latestStartRaidDateTime = this.getRaidStartAt();

        if (latestStartRaidDateTime == null) {
            return true;
        }

        int raidTime = this.getRaidingMinute();
        return current.isAfter(latestStartRaidDateTime.plusMinutes(raidTime));
    }

    public void validateRaidEnter(LocalDateTime current) throws RaidTimeoutException {
        if (!isTimeOut(current)) {
            throw new RaidTimeoutException("?????? ???????????? ??????????????????");
        }
        if (!isTimeOut(current) && this.isRaiding()) {
            throw new RaidTimeoutException("?????? ???????????? ??????????????????");
        }
    }
    public void validateRaidEnd(RaidEndRequestDto requestDto) throws RaidTimeoutException {
        if (isTimeOut(requestDto.getRaidEndTime())) {
            throw new RaidTimeoutException("????????? ????????? ????????? ???????????????");
        }
        if (!this.getLatestRaidUserId().equals(requestDto.getUserId())) {
            throw new IllegalArgumentException("????????? ???????????? ???????????? ?????? ???????????? ????????????. raidUserId: %s, requestUserId: %s");
        }
    }

    public BossStateResponseDto toBossStateResponseDto(boolean canEnter) {
        return BossStateResponseDto.builder()
                .canEnter(canEnter)
                .enteredUserId(this.latestRaidUserId)
                .build();
    }
}
