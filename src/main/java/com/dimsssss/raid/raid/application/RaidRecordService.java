package com.dimsssss.raid.raid.application;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RaidRecordService {
    private final RaidManagement raidManagement;
    private final BossStateRepository bossStateRepository;
    private final RaidRecordRepository raidRecordRepository;

    @Transactional
    public RaidStartResponseDto startRaid (RaidStartRequestDto requestDto) throws NotCantRaidException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();
        LocalDateTime startTime = LocalDateTime.now();

        raidManagement.validateRaidEnter(bossStateEntity, startTime);
        raidManagement.enterRaid(bossStateEntity, startTime);

        RaidRecordEntity result = raidRecordRepository.save(requestDto.convertFrom());
        return result.toResponse();
    }
}
