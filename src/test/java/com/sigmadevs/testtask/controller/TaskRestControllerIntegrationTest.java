package com.sigmadevs.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.dto.UpdateTaskDTO;
import com.sigmadevs.testtask.app.entity.Task;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql", "/sql/insert_tasks.sql"})
public class TaskRestControllerIntegrationTest {
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

    Task task;
    TaskDTO taskDTO;
    CreateTaskDTO createTaskDTO;
    UpdateTaskDTO updateTaskDTO;

    @BeforeEach
    void setUp() {

        task = Task.builder()
                .title("Test title")
                .build();

        taskDTO = TaskDTO.builder()
                .title("Test title")
                .build();

        createTaskDTO = CreateTaskDTO
                .builder()
                .title("Test title")
                .questId(1L)
                .build();

        updateTaskDTO = UpdateTaskDTO.builder()
                .id(1L)
                .title("Test title 2")
                .build();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void createTask_shouldReturnCreatedStatus(Long questId) throws Exception {
        createTaskDTO.setQuestId(questId);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    void createTask_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {
        createTaskDTO.setTitle(null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));
    }

    @Test
    void createTask_shouldReturnNotFoundStatus_whenQuestNotFound() throws Exception {
        createTaskDTO.setQuestId(100L);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Quest with Id " + createTaskDTO.getQuestId() + " not found!"));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void updateTask_shouldReturnOkStatus(Long id) throws Exception {
        updateTaskDTO.setId(id);

        mockMvc.perform(put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    void updateTask_shouldReturnUnprocessableEntityStatus_whenIdIsNull() throws Exception {
        updateTaskDTO.setId(null);

        mockMvc.perform(put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains Id!"));
    }

    @Test
    void getAllTasks_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void getTaskById_shouldReturnOkStatus(Long taskId) throws Exception {
        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));
    }

    @Test
    void getTaskById_shouldReturnNotFoundStatus_whenTaskNotFound() throws Exception {
        Long id = 100L;

        mockMvc.perform(get("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Task with Id " + id + " not found!"));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void removeTaskById_shouldReturnNoContentStatus(Long taskId) throws Exception {
        mockMvc.perform(delete("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeTaskById_shouldReturnNotFoundStatus_whenTaskNotFound() throws Exception {
        Long id = 100L;

        mockMvc.perform(delete("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Task with Id " + id + " not found!"));
    }
}
