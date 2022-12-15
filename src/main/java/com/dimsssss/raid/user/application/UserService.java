package com.dimsssss.raid.user.application;


import com.dimsssss.raid.raid.domain.RaidRecordEntity;
import com.dimsssss.raid.raid.domain.RaidRecordRepository;
import com.dimsssss.raid.raid.domain.RankingRepositoryImple;
import com.dimsssss.raid.user.application.dto.UserInformationResponseDto;
import com.dimsssss.raid.user.domain.RaidHistory;
import com.dimsssss.raid.user.domain.UserRepository;
import com.dimsssss.raid.user.presentation.dto.UserSignupRequestDto;
import com.dimsssss.raid.user.presentation.dto.UserSignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RankingRepositoryImple rankingRepositoryImple;
    private final RaidRecordRepository raidRecordRepository;

    public UserSignupResponseDto signUp(UserSignupRequestDto requestDto) {
        return new UserSignupResponseDto(userRepository.save(requestDto.convertFrom()));
    }

    public UserInformationResponseDto getUserHistory(Long userId) {
        List<RaidHistory> historyEntities = raidRecordRepository.findByUserId(userId)
                .stream().map(RaidRecordEntity::toRaidHistory).collect(Collectors.toList());
        int score = rankingRepositoryImple.getPrevScore(userId);
        return new UserInformationResponseDto(score, historyEntities);
    }
}
