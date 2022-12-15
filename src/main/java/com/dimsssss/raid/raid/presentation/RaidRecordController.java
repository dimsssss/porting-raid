package com.dimsssss.raid.raid.presentation;

import com.dimsssss.raid.raid.application.RaidRecordService;
import com.dimsssss.raid.raid.domain.RaidTimeoutException;
import com.dimsssss.raid.raid.domain.dto.BossStateResponseDto;
import com.dimsssss.raid.raid.domain.dto.RaidStartResponseDto;
import com.dimsssss.raid.raid.domain.dto.RankingResponseDto;
import com.dimsssss.raid.raid.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
public class RaidRecordController {
    private final RaidRecordService raidRecordService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bossRaid/enter")
    public RaidStartResponseDto start(@RequestBody RaidStartRequestDto requestDto) {
        try {
            return raidRecordService.startRaid(requestDto);
        } catch (RaidTimeoutException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/bossRaid/end")
    public void end(@RequestBody RaidEndRequestDto requestDto) {
        try {
            raidRecordService.endRaid(requestDto);
        } catch (RaidTimeoutException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/bossRaid/topRankerList", method = RequestMethod.GET)
    public RankingResponseDto getRankers(@RequestBody RankingRequestDto requestDto) {
        return raidRecordService.getRankigList(requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/bossRaid")
    public BossStateResponseDto getBossState() {
        try {
            BossStateResponseDto bossStateResponseDto = raidRecordService.getBossState();
            return bossStateResponseDto;
        } catch (RaidTimeoutException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
}
