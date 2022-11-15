package com.dimsssss.raid.raid.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RaidRecordRepositoryTest {

    @Autowired
    private RaidRecordRepository raidRecordRepository;

    @AfterAll
    void clean() {
        raidRecordRepository.deleteAll();
    }

    @DisplayName("보스 레이드 내용이 저장된다")
    @Test
    public void save() {
        Long userId = 1L;
        int score = 200;
        RaidRecordEntity raidRecordEntity = RaidRecordEntity.builder()
                .userId(userId)
                .score(score)
                .build();
        raidRecordRepository.save(raidRecordEntity);
        RaidRecordEntity raidRecord = raidRecordRepository.findAll().get(0);

        assertThat(raidRecord.getRecordId()).isGreaterThan(0L);
        assertThat(raidRecord.getUserId()).isEqualTo(userId);
        assertThat(raidRecord.getScore()).isEqualTo(score);
    }
}