package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.domain.dto.RaidStartResponseDto;
import com.dimsssss.raid.user.domain.RaidHistory;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class RaidRecordEntity {
    @GeneratedValue
    @Id
    private Long raidRecordId;
    @Column(name = "user_id")
    private Long userId;
    @Column
    private Integer score;

    @CreationTimestamp
    @Column(name = "raid_start_at")
    private LocalDateTime raidStartAt;
    @Column(name = "raid_end_at")
    private LocalDateTime raidEndAt;

    @Builder
    public RaidRecordEntity(Long userId, int score, Long raidRecordId) {
        this.userId = userId;
        this.score = score;
        this.raidRecordId = raidRecordId;
    }
    public RaidStartResponseDto toResponse() {
        return RaidStartResponseDto.builder()
                .raidRecordId(this.raidRecordId)
                .canEnter(true)
                .build();
    }

    private RaidRecordEntity(Long raidRecordId, Long userId, Integer score, LocalDateTime raidStartAt, LocalDateTime raidEndAt) {
        this.raidRecordId = raidRecordId;
        this.userId = userId;
        this.score = score;
        this.raidStartAt = raidStartAt;
        this.raidEndAt = raidEndAt;
    }

    public RaidRecordEntity withRaidEndTime(LocalDateTime endTime) {
        return new RaidRecordEntity(raidRecordId, userId, score, raidStartAt, endTime);
    }

    public RaidHistory toRaidHistory() {
        return RaidHistory.builder()
                .raidRecordId(this.raidRecordId)
                .enterTime(this.raidStartAt)
                .endTime(this.raidEndAt)
                .score(this.score)
                .build();
    }

}
