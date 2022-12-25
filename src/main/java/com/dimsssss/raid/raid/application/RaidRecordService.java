package com.dimsssss.raid.raid.application;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.domain.dto.BossStateResponseDto;
import com.dimsssss.raid.raid.domain.dto.RaidStartResponseDto;
import com.dimsssss.raid.raid.domain.dto.RankingResponseDto;
import com.dimsssss.raid.raid.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RaidRecordService {
    private final BossStateRepository bossStateRepository;
    private final RaidRecordRepository raidRecordRepository;
    private final RankingRepositoryImple rankingRepositoryImple;

    @Transactional
    public RaidStartResponseDto startRaid (RaidStartRequestDto requestDto) throws RaidTimeoutException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();

        bossStateEntity.validateRaidEnter(requestDto.getRaidStartTime());

        bossStateRepository.save(bossStateEntity.withRaidingStateAndStartTime(true, requestDto.getRaidStartTime()));
        RaidRecordEntity result = raidRecordRepository.save(requestDto.convertFrom());
        return result.toResponse();
    }

    @Transactional
    public void endRaid (RaidEndRequestDto requestDto) throws NotFoundRaidRecordException, RaidTimeoutException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();

        bossStateEntity.validateRaidEnd(requestDto);

        Optional<RaidRecordEntity> raidRecordEntity = raidRecordRepository.findById(requestDto.getRaidRecordId());

        if (raidRecordEntity.isEmpty()) {
            throw new NotFoundRaidRecordException("존재하지 않는 raidRecordId 입니다 : " + requestDto.getRaidRecordId());
        }

        bossStateRepository.save(bossStateEntity.withRaidingState(false));
        raidRecordRepository.save(raidRecordEntity.get().withRaidEndTime(requestDto.getRaidEndTime()));
        rankingRepositoryImple.save(raidRecordEntity.get());
    }

    public RankingResponseDto getRankingList(RankingRequestDto requestDto) {
        return rankingRepositoryImple.getTopRankingAndMyRanking(requestDto.getUserId());
    }

    public BossStateResponseDto getBossState() throws RaidTimeoutException {
        BossStateEntity bossStateEntity = bossStateRepository.findBossState();
        bossStateEntity.validateRaidEnter(LocalDateTime.now());
        return bossStateEntity.toBossStateResponseDto(true);
    }
}
