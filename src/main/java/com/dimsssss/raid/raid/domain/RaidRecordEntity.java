package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.raid.presentation.dto.RaidStartResponseDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Long raidRecordId;
    @Column
    private Long userId;
    @Column
    private int score;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

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

}
