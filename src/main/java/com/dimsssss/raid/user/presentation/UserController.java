package com.dimsssss.raid.user.presentation;

import com.dimsssss.raid.user.application.UserService;
import com.dimsssss.raid.user.application.dto.UserInformationResponseDto;
import com.dimsssss.raid.user.presentation.dto.UserSignupRequestDto;
import com.dimsssss.raid.user.presentation.dto.UserSignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserSignupResponseDto signUp(@RequestBody UserSignupRequestDto requestDto) {
        return userService.signUp(requestDto);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserInformationResponseDto getUserInformation(@PathVariable Long userId) {
        return userService.getUserHistory(userId);
    }
}
