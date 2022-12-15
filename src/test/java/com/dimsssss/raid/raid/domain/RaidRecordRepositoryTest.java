package com.dimsssss.raid.raid.domain;

import com.dimsssss.raid.user.domain.RaidHistory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RaidRecordRepositoryTest {
    @Autowired
    private RaidRecordRepository raidRecordRepository;

    @Autowired
    private BossStateRepository bossStateRepository;

    @BeforeAll
    void setup() {
        BossStateEntity bossStateEntity = BossStateEntity.builder().build();
        bossStateRepository.save(bossStateEntity);

        List<RaidRecordEntity> records = new ArrayList<>();

        records.add(RaidRecordEntity.builder().raidRecordId(1L).userId(1L).build());
        records.add(RaidRecordEntity.builder().raidRecordId(2L).userId(1L).build());
        records.add(RaidRecordEntity.builder().raidRecordId(3L).userId(1L).build());
        records.add(RaidRecordEntity.builder().raidRecordId(4L).userId(1L).build());
        records.add(RaidRecordEntity.builder().raidRecordId(5L).userId(1L).build());

        raidRecordRepository.saveAll(records);
    }

    @AfterAll
    void clean() {
        raidRecordRepository.deleteAll();
    }

    @DisplayName("보스 레이드 내용이 저장된다")
    @Test
    public void save() {
        Long userId = 2L;
        int score = 3;
        RaidRecordEntity raidRecordEntity = RaidRecordEntity.builder()
                .raidRecordId(10L)
                .userId(userId)
                .score(score)
                .build();
        raidRecordRepository.save(raidRecordEntity);
        Optional<RaidRecordEntity> raidRecord = raidRecordRepository.findById(10L);
        raidRecord.ifPresent(record -> {
            assertThat(record.getRaidRecordId()).isGreaterThan(0L);
            assertThat(record.getUserId()).isEqualTo(userId);
            assertThat(record.getScore()).isEqualTo(score);
        });
    }

    @DisplayName("유저가 raid한 모든 기록을 가져온다")
    @Test
    public void findByUserId() {
        List<RaidHistory> records = raidRecordRepository.findByUserId(1L).stream().map(RaidRecordEntity::toRaidHistory).collect(Collectors.toList());
        for (RaidHistory raidRecordEntity : records) {
            assertThat(raidRecordEntity.getRaidRecordId()).isGreaterThan(0L);
        }
    }
}