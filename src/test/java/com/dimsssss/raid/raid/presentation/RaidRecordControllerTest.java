package com.dimsssss.raid.raid.presentation;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RaidRecordControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BossStateRepository bossStateRepository;
    @MockBean
    RaidRecordRepository raidRecordRepository;

    @DisplayName("POST /bossraid/enter 호출 시 응답 값과 201을 반환한다")
    @WithMockUser
    @Test
    public void start() throws Exception {
        RaidStartRequestDto requestDto = new RaidStartRequestDto(1L, 3);
        BossStateEntity bossStateEntity = Mockito.mock(BossStateEntity.class);
        RaidRecordEntity result = new RaidRecordEntity(1L, 3, 1L);

        List<BossStateEntity> bossStateEntities = new ArrayList<>();
        bossStateEntities.add(bossStateEntity);
        Mockito.when(bossStateRepository.findAll()).thenReturn(bossStateEntities);
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        Mockito.when(raidRecordRepository.save(any(RaidRecordEntity.class))).thenReturn(result);

        String url = "http://localhost:" + port + "/bossraid/enter";

        mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto))
                    .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.raidRecordId", greaterThan(0)))
            .andExpect(jsonPath("$.canEnter", is(true)));
    }

    @DisplayName("POST /bossraid/enter : 데이터베이스 예외 발생시 500 반환")
    @WithMockUser
    @Test
    public void start_ () throws Exception {
        RaidStartRequestDto requestDto = new RaidStartRequestDto(1L, 3);
        BossStateEntity bossStateEntity = Mockito.mock(BossStateEntity.class);

        List<BossStateEntity> bossStateEntities = new ArrayList<>();
        bossStateEntities.add(bossStateEntity);
        Mockito.when(bossStateRepository.findAll()).thenReturn(bossStateEntities);
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        doThrow(ObjectOptimisticLockingFailureException.class)
                .when(bossStateEntity).onRaid();

        String url = "http://localhost:" + port + "/bossraid/enter";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().is5xxServerError());
    }
}