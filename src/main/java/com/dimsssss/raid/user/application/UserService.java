package com.dimsssss.raid.user.application;


import com.dimsssss.raid.user.domain.UserRepository;
import com.dimsssss.raid.user.presentation.dto.UserSignupRequestDto;
import com.dimsssss.raid.user.presentation.dto.UserSignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserSignupResponseDto signUp(UserSignupRequestDto requestDto) {
        return new UserSignupResponseDto(userRepository.save(requestDto.convertFrom()));
    }
}
