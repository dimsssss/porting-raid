package com.dimsssss.raid.raid.presentation;

import com.dimsssss.raid.raid.application.RaidRecordService;
import com.dimsssss.raid.raid.domain.NotCantRaidException;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
public class RaidRecordController {
    private final RaidRecordService raidRecordService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bossraid/enter")
    public RaidStartResponseDto start(@RequestBody RaidStartRequestDto requestDto) {
        try {
            return raidRecordService.startRaid(requestDto);
        } catch (NotCantRaidException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
