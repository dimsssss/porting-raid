package com.dimsssss.raid.raid.presentation;

import com.dimsssss.raid.raid.domain.*;
import com.dimsssss.raid.raid.presentation.dto.RaidEndRequestDto;
import com.dimsssss.raid.raid.presentation.dto.RaidStartRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    RankingRepositoryImple rankingRepositoryImple;

    ObjectMapper objectMapper;

    @BeforeAll
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("POST /bossRaid/enter ?????? ??? ?????? ?????? 201??? ????????????")
    @WithMockUser
    @Test
    public void start() throws Exception {
        RaidStartRequestDto requestDto = new RaidStartRequestDto(1L, 3, LocalDateTime.now());
        BossStateEntity bossStateEntity = Mockito.mock(BossStateEntity.class);
        RaidRecordEntity result = new RaidRecordEntity(1L, 3, 1L);

        List<BossStateEntity> bossStateEntities = new ArrayList<>();
        bossStateEntities.add(bossStateEntity);
        Mockito.when(bossStateRepository.findAll()).thenReturn(bossStateEntities);
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        Mockito.when(raidRecordRepository.save(any(RaidRecordEntity.class))).thenReturn(result);

        String url = "http://localhost:" + port + "/bossRaid/enter";



        mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.raidRecordId", greaterThan(0)))
            .andExpect(jsonPath("$.canEnter", is(true)));
    }

    @DisplayName("POST /bossRaid/enter : ObjectOptimisticLockingFailureException ?????? ????????? http status 500 ??????")
    @WithMockUser
    @Test
    public void start_fail_when_database_exception () throws Exception {
        RaidStartRequestDto requestDto = new RaidStartRequestDto(1L, 3, LocalDateTime.now());
        BossStateEntity bossStateEntity = Mockito.mock(BossStateEntity.class);

        Mockito.when(bossStateEntity.withRaidingStateAndStartTime(any(Boolean.class), any(LocalDateTime.class))).thenReturn(bossStateEntity);
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        Mockito.when(bossStateRepository.save(bossStateEntity)).thenThrow(ObjectOptimisticLockingFailureException.class);

        String url = "http://localhost:" + port + "/bossRaid/enter";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().is5xxServerError());
    }
    @DisplayName("PATCH /bossRaid/end : ????????? ????????? http status 200 ??????")
    @WithMockUser
    @Test
    public void end () throws Exception {
        RaidEndRequestDto requestDto = RaidEndRequestDto
                .builder()
                .userId(1L)
                .raidRecordId(1L)
                .bossStateId(1L)
                .build();

        String url = "http://localhost:" + port + "/bossRaid/end";
        RankingEntity rankingEntity = RankingEntity.builder()
                .userId(1L)
                .totalScore(20)
                .build();
        BossStateEntity bossStateEntity = Mockito.mock(BossStateEntity.class);
        RaidRecordEntity raidRecordEntity = RaidRecordEntity.builder()
                .raidRecordId(1L)
                .userId(1L)
                .score(20)
                .build();
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        Mockito.when(raidRecordRepository.findById(requestDto.getRaidRecordId())).thenReturn(Optional.of(raidRecordEntity));
        doNothing().when(rankingRepositoryImple).save(raidRecordEntity);

        mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("GET /bossRaid : ???????????? ???????????? ?????? ?????? http status 200 ??????")
    @WithMockUser
    @Test
    public void getBossState () throws Exception {
        String url = "http://localhost:" + port + "/bossRaid";
        BossStateEntity bossStateEntity = new BossStateEntity();
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        mockMvc.perform(get(url).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canEnter", is(true)))
                .andExpect(jsonPath("$.enteredUserId", is(nullValue())));
    }

    @DisplayName("GET /bossRaid : ???????????? ??????????????? ?????? ?????? http status 400 ??????")
    @WithMockUser
    @Test
    public void getBossState_fail_when_raiding () throws Exception {
        String url = "http://localhost:" + port + "/bossRaid";
        BossStateEntity bossStateEntity = new BossStateEntity().withRaidingStateAndStartTime(true, LocalDateTime.now()).withUserId(1L);
        Mockito.when(bossStateRepository.findBossState()).thenReturn(bossStateEntity);
        mockMvc.perform(get(url).with(csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason(containsString("?????? ???????????? ??????????????????")));
    }
}