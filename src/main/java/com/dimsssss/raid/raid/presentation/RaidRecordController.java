package com.dimsssss.raid.raid.presentation;

import com.dimsssss.raid.raid.application.RaidRecordService;
import com.dimsssss.raid.raid.domain.RaidTimeoutException;
import com.dimsssss.raid.raid.presentation.dto.RaidEndRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartResponseDto;
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
}
