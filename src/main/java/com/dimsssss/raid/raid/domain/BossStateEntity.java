package com.dimsssss.raid.raid.domain;

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
    private int raidingMinute;
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

    public void onRaid() {
        isRaiding = true;
    }

    public void offRaid() {
        isRaiding = false;
    }

    public void setRaidStartAt(LocalDateTime raidStartAt) {
        this.raidStartAt = raidStartAt;
    }
}
