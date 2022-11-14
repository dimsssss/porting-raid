package com.dimsssss.raid.user.presentation;

import com.dimsssss.raid.user.presentation.dto.UserSignupRequestDto;
import com.dimsssss.raid.user.presentation.dto.UserSignupResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "security.basic.enabled=false"
        })
class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void signUp() {
        String email = "test@test.com";
        String password = "1234";
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user";

        ResponseEntity<UserSignupResponseDto> responseEntity = restTemplate.postForEntity(url, requestDto, UserSignupResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getId()).isGreaterThan(0L);
        assertThat(responseEntity.getBody().getEmail()).isEqualTo(email);
    }
}