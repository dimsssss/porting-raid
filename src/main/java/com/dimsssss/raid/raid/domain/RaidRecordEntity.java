package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class RaidRecordEntity {
    @GeneratedValue
    @Id
    private Long recordId;
    @Column
    private Long userId;
    @Column
    private int score;
    @Column
    private LocalDateTime raidStartAt;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime deletedAt;

    @Builder
    public RaidRecordEntity(Long userId, int score) {
        this.userId = userId;
        this.score = score;
    }
}
