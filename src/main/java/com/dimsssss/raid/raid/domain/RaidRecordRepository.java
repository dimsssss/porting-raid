package com.dimsssss.raid.raid.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RaidRecordRepository extends JpaRepository<RaidRecordEntity, Long> {
    @Query(value = "SELECT raid_record_id, score, raid_start_at, raid_end_at, user_id FROM raid_record_entity WHERE user_id = :userId", nativeQuery = true)
    List<RaidRecordEntity> findByUserId(@Param("userId") Long userId);
}
