package com.sigmadevs.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigmadevs.testtask.app.dto.CreateOptionDTO;
import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.UpdateOptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql" , "/sql/insert_quests.sql" , "/sql/insert_tasks.sql", "/sql/insert_options.sql"})
public class OptionRestControllerIntegrationTest {
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

    Option option;
    OptionDTO optionDTO;
    CreateOptionDTO createOptionDTO;
    UpdateOptionDTO updateOptionDTO;

    @BeforeEach
    void setUp() {
        option = Option.builder()
                .title("Test option")
                .build();

        optionDTO = OptionDTO.builder()
                .title("Test option")
                .isTrue(true)
                .build();

        createOptionDTO = CreateOptionDTO.builder()
                .title("Test option")
//                .taskId(1L)
                .isTrue(true)
                .build();

        updateOptionDTO = UpdateOptionDTO.builder()
                .id(1L)
                .title("Test option 2")
                .isTrue(true)
                .build();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void createOption_shouldReturnCreatedStatus(Long taskId) throws Exception {

//        createOptionDTO.setTaskId(taskId);

        mockMvc.perform(post("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOptionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void createOption_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {
        createOptionDTO.setTitle(null);

        mockMvc.perform(post("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));
    }

    @Test
    void createOption_shouldReturnUnprocessableEntityStatus_whenTaskIsNull() throws Exception {

//        createOptionDTO.setTaskId(null);

        mockMvc.perform(post("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains task Id!"));
    }
    @Test
    void createOption_shouldReturnUnprocessableEntityStatus_whenIsTrueIsNull() throws Exception {

        createOptionDTO.setIsTrue(null);

        mockMvc.perform(post("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Option should be true or false!"));
    }

    @Test
    void createOption_shouldReturnNotFoundStatus_whenTaskNotFound() throws Exception {

//        createOptionDTO.setTaskId(100L);

        mockMvc.perform(post("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOptionDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404));
//                .andExpect(jsonPath("$.message").value("Task with Id " + createOptionDTO.getTaskId() + " not found!"));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void updateOption_shouldReturnOkStatus(Long id) throws Exception {
        updateOptionDTO.setId(id);

        mockMvc.perform(put("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOptionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void updateOption_shouldReturnUnprocessableEntityStatus_whenIdIsNull() throws Exception {
        updateOptionDTO.setId(null);

        mockMvc.perform(put("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains Id!"));
    }

    @Test
    void updateOption_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {
        updateOptionDTO.setTitle(null);

        mockMvc.perform(put("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));
    }

    @Test
    void updateOption_shouldReturnUnprocessableEntityStatus_whenIsTrueIsNull() throws Exception {

        updateOptionDTO.setIsTrue(null);

        mockMvc.perform(put("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOptionDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Option should be true or false!"));
    }

    @Test
    void updateOption_shouldReturnNotFoundStatus_whenOptionNotFound() throws Exception {
        updateOptionDTO.setId(100L);

        mockMvc.perform(put("/api/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOptionDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Option with Id " + updateOptionDTO.getId() + " not found!"));
    }

    @Test
    void getAllOptions_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/api/options")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void getOptionById_shouldReturnOkStatus(Long optionId) throws Exception {

        mockMvc.perform(get("/api/options/" + optionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.id").value(optionId));
    }
    @Test
    void getOptionById_shouldReturnNotFoundStatus_whenOptionNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(get("/api/options/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Option with Id " + id + " not found!"));
    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void removeOptionById_shouldReturnNoContentStatus(Long id) throws Exception {

        mockMvc.perform(delete("/api/options/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeOptionById_shouldReturnNoFoundStatus_whenOptionNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(delete("/api/options/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
