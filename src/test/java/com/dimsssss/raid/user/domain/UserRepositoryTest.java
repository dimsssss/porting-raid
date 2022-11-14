package com.dimsssss.raid.user.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterAll
    void clean() {
        userRepository.deleteAll();
    }

    @DisplayName("유저의 가입 정보가 저장된다")
    @Test
    public void create() {
        String email = "test@test.com";
        String password = "password";

        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .build());

        User user = userRepository.findAll().get(0);

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getId()).isGreaterThan(0L);
    }
}
