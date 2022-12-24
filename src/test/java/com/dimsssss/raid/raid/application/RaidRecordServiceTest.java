package com.dimsssss.raid.raid.application;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.domain.dto.BossStateResponseDto;
import com.dimsssss.raid.raid.presentation.dto.RaidEndRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.dimsssss.raid.raid.domain.dto.RaidStartResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RaidRecordServiceTest {

    @Autowired
    RaidRecordService raidRecordService;

    @MockBean
    BossStateRepository bossStateRepository;

    @MockBean
    RaidRecordRepository raidRecordRepository;

    RaidStartRequestDto requestDto;

    BossStateEntity bossStateEntity;

    RaidRecordEntity raidRecordEntity;

    RaidEndRequestDto raidEndRequestDto;

    @BeforeEach
    void setup() {
        requestDto = RaidStartRequestDto.builder()
                .userId(1L)
                .level(3)
                .build();

        raidRecordEntity = RaidRecordEntity.builder()
                .raidRecordId(1L)
                .userId(1L)
                .score(10)
                .build();

        raidEndRequestDto = RaidEndRequestDto.builder()
                .raidRecordId(1L)
                .userId(1L)
                .bossStateId(1L)
                .build();
        Mockito.when(raidRecordRepository.save(any(RaidRecordEntity.class))).thenReturn(raidRecordEntity);
    }

    @Test
    @DisplayName("시작 시간이 없고 진행중인 레이드가 없다면 레이드가 가능하다")
    void startRaid() throws RaidTimeoutException {
        RaidStartResponseDto responseDto = raidRecordService.startRaid(requestDto);

        assertThat(responseDto.getRaidRecordId()).isEqualTo(1L);
        assertThat(responseDto.isCanEnter()).isTrue();
    }

    @DisplayName("raid 시간 내에 다른 raid 시작 요청이 오면 예외를 반환한다")
    @Test
    void startRaid_fail_when_raid_on() {
        bossStateEntity.enterRaid(LocalDateTime.now());
        bossStateEntity.onRaid();

        assertThatThrownBy(() -> raidRecordService.startRaid(requestDto))
                .isInstanceOf(RaidTimeoutException.class)
                .hasMessage("현재 레이드가 진행중입니다");
    }

    @DisplayName("raid 시간이 종료되면 새로운 raid 가 가능하다")
    @Test
    void startRaid_success_when_time_out() throws RaidTimeoutException {
        bossStateEntity.enterRaid(LocalDateTime.of(2022, 12, 11, 1,1,1));
        bossStateEntity.onRaid();

        RaidStartResponseDto responseDto = raidRecordService.startRaid(requestDto);

        assertThat(responseDto.getRaidRecordId()).isEqualTo(1L);
        assertThat(responseDto.isCanEnter()).isTrue();
    }

    @DisplayName("허용 시간이 끝나지 않았고 raid 종료 사용자와 raid 중인 사용자가 일치하면 void ")
    @Test
    void endRaid_success() {

    }

    @DisplayName("raid 시간이 남지 않았을 때 종료 요청이 오면 예외를 반환한다")
    @Test
    void endRaid_fail_when_time_out() {
        bossStateEntity.enterRaid(LocalDateTime.of(2022, 12, 11, 1,1,1));
        assertThatThrownBy(() -> raidRecordService.endRaid(raidEndRequestDto))
                .isInstanceOf(RaidTimeoutException.class)
                .hasMessage("주어진 레이드 시간이 지났습니다");
    }

    @DisplayName("raid 종료 요청자와 raid 진행중인 사용자가 다르면 예외를 반환한다")
    @Test
    void endRaid_fail_when_raider_not_be_requester() {
        bossStateEntity.setRaidStartAt(LocalDateTime.now());
        RaidEndRequestDto otherRequestDto = RaidEndRequestDto.builder()
                .raidRecordId(1L)
                .userId(2L)
                .bossStateId(1L)
                .build();

        assertThatThrownBy(() -> raidRecordService.endRaid(otherRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("레이드 진행중인 아이디와 종료 아이디가 다릅니다. raidUserId: %s, requestUserId: %s");
    }

    @DisplayName("현재 레이드가 진행중일 경우 예외 처리한다")
    @Test
    void getBossState_not_cant_enter() {
        bossStateEntity.setRaidStartAt(LocalDateTime.now());
        bossStateEntity.onRaid();
        assertThatThrownBy(() -> raidRecordService.getBossState())
                .isInstanceOf(RaidTimeoutException.class)
                .hasMessage("현재 레이드가 진행중입니다");
    }

    @DisplayName("현재 레이드가 가능할 때 enteredUserId, canEnter 값을 반환한다")
    @Test
    void getBossState_can_enter() throws Exception {
        BossStateResponseDto bossStateResponseDto = raidRecordService.getBossState();
        assertThat(bossStateResponseDto.isCanEnter()).isTrue();
        assertThat(bossStateResponseDto.getEnteredUserId()).isEqualTo(1L);
    }
}