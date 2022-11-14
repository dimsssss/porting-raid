package com.dimsssss.raid.user.presentation.dto;

import com.dimsssss.raid.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignupRequestDto {
    private String email;
    private String password;

    @Builder
    public UserSignupRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User convertFrom() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
