package com.sigmadevs.testtask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql"})
public class QuestRestControllerIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    Quest quest;
    QuestDTO questDTO;
    CreateQuestDTO createQuestDTO;
    UpdateQuestDTO updateQuestDTO;
    @BeforeEach
    void setUp() {

        quest = Quest.builder()
                .title("Test title")
                .build();

        questDTO = QuestDTO.builder()
                .title("Test title")
                .build();

        createQuestDTO = CreateQuestDTO.builder()
                .title("Test title")
                .userId(1L)
                .build();

        updateQuestDTO = UpdateQuestDTO.builder()
                .id(1L)
                .title("Test title 2")
                .build();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void createQuest_shouldReturnCreatedStatus(Long userId) throws Exception {

        createQuestDTO.setUserId(userId);

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(9));

    }

    @Test
    void createQuest_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {

        createQuestDTO.setTitle(null);

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));;

    }
    @Test
    void createQuest_shouldReturnUnprocessableEntityStatus_whenUserIdIsNull() throws Exception {

        createQuestDTO.setUserId(null);

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains user Id!"));;

    }

    @Test
    void createQuest_shouldReturnNotFoundStatus_whenUserNotFound() throws Exception {

        createQuestDTO.setUserId(100L);

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User with " + createQuestDTO.getUserId() + " Id not found!"));;

    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void updateQuest_shouldReturnOkdStatus(Long id) throws Exception {

        updateQuestDTO.setId(id);

        mockMvc.perform(put("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateQuestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9));

    }
    @Test
    void updateQuest_shouldReturnUnprocessableEntityStatus_whenIdIsNull() throws Exception {

        updateQuestDTO.setId(null);

        mockMvc.perform(put("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateQuestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains Id!"));;

    }
    @Test
    void updateQuest_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {

        updateQuestDTO.setTitle(null);

        mockMvc.perform(put("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateQuestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));;

    }

    @Test
    void updateQuest_shouldReturnNotFoundStatus_whenQuestNotFound() throws Exception {

        updateQuestDTO.setId(100L);

        mockMvc.perform(put("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateQuestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Quest with Id " + updateQuestDTO.getId() + " not found!"));;

    }
    @Test
    void getAllQuests_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void getQuestById_shouldReturnOkStatus(Long questId) throws Exception {

        mockMvc.perform(get("/api/quests/" + questId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$.id").value(questId));

    }
    @Test
    void getQuestById_shouldReturnNotFoundStatus_whenQuestNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(get("/api/quests/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Quest with Id " + id + " not found!"));

    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void removeQuestById_shouldReturnNoContentStatus(Long questId) throws Exception {

        mockMvc.perform(delete("/api/quests/" + questId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
    @Test
    void removeQuestById_shouldReturnNotFoundStatus_whenQuestNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(delete("/api/quests/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Quest with Id " + id + " not found!"));

    }

}
