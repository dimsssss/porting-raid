package com.dimsssss.raid.user.presentation.dto;

import com.dimsssss.raid.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserSignupResponseDto {
    private Long id;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public UserSignupResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
