package com.dimsssss.raid.raid.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BossStateRepository extends JpaRepository<BossStateEntity, Long> {
    @Query(value = "SELECT * FROM boss_state LIMIT 1", nativeQuery = true)
    BossStateEntity findBossState();
}
