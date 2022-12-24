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
class BossStateRepositoryTest {
    @Autowired
    private BossStateRepository bossStateRepository;

    @AfterAll
    void clean() {
        bossStateRepository.deleteAll();
    }

    @DisplayName("보스의 상태를 저장할 수 있다")
    @Test
    public void create() {
        BossStateEntity bossStateEntity = new BossStateEntity();
        bossStateRepository.save(bossStateEntity);

        BossStateEntity result = bossStateRepository.findAll().get(0);
        assertThat(result.getBossStateId()).isGreaterThan(0L);
    }
}