package com.dimsssss.raid.raid.application;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.presentation.dto.RaidEndRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RaidRecordService {
    private final BossStateRepository bossStateRepository;
    private final RaidRecordRepository raidRecordRepository;

    @Transactional
    public RaidStartResponseDto startRaid (RaidStartRequestDto requestDto) throws RaidTimeoutException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();
        LocalDateTime startTime = LocalDateTime.now();

        bossStateEntity.validateRaidEnter(startTime);
        bossStateEntity.enterRaid(startTime);

        RaidRecordEntity result = raidRecordRepository.save(requestDto.convertFrom());
        return result.toResponse();
    }

    @Transactional
    public void endRaid (RaidEndRequestDto requestDto) throws RaidTimeoutException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();
        LocalDateTime endTime = LocalDateTime.now();

        bossStateEntity.validateRaidEnd(endTime, requestDto.getUserId());
        bossStateEntity.offRaid();
        RaidRecordEntity raidRecordEntity = raidRecordRepository.findById(requestDto.getRaidRecordId()).orElseThrow(() -> {
            throw new IllegalArgumentException("존재 하지 않는 Record입니다. raidRecordId = " + requestDto.getRaidRecordId());
        });

        raidRecordEntity.logRaidEndTime(endTime);
    }
}
