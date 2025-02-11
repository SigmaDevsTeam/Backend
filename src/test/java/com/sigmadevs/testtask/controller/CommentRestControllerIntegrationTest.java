package com.sigmadevs.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.WriteCommentDTO;
import com.sigmadevs.testtask.app.dto.EditCommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql" , "/sql/insert_quests.sql" , "/sql/insert_tasks.sql", "/sql/insert_options.sql", "/sql/insert_comments.sql"})
public class CommentRestControllerIntegrationTest {
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

    Comment comment;
    CommentDTO commentDTO;
    WriteCommentDTO writeCommentDTO;
    EditCommentDTO editCommentDTO;

    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .title("Test comment")
                .build();

        commentDTO = CommentDTO.builder()
                .title("Test comment")
                .build();

        writeCommentDTO = WriteCommentDTO.builder()
                .title("Test comment")
//                .userId(1L)
                .questId(1L)
                .build();

        editCommentDTO = EditCommentDTO.builder()
                .id(1L)
                .title("Updated comment")
                .build();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void createComment_shouldReturnCreatedStatus(Long userId) throws Exception {
//        writeCommentDTO.setUserId(userId);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void createComment_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {
        writeCommentDTO.setTitle(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));
    }

    @Test
    void createComment_shouldReturnUnprocessableEntityStatus_whenUserIdIsNull() throws Exception {
//        writeCommentDTO.setUserId(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains user Id!"));
    }

    @Test
    void createComment_shouldReturnUnprocessableEntityStatus_whenQuestIdIsNull() throws Exception {
        writeCommentDTO.setQuestId(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains quest Id!"));
    }

    @Test
    void createComment_shouldReturnNotFoundStatus_whenUserNotFound() throws Exception {
//        writeCommentDTO.setUserId(100L);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404));
//                .andExpect(jsonPath("$.message").value("User with " + writeCommentDTO.getUserId() + " Id not found!"));
    }

    @Test
    void createComment_shouldReturnNotFoundStatus_whenQuestNotFound() throws Exception {
        writeCommentDTO.setQuestId(100L);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeCommentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Quest with Id " + writeCommentDTO.getQuestId() + " not found!"));
    }


    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void updateComment_shouldReturnOkStatus(Long id) throws Exception {
        editCommentDTO.setId(id);

        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void updateComment_shouldReturnUnprocessableEntityStatus_whenIdIsNull() throws Exception {
        editCommentDTO.setId(null);

        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains Id!"));
    }

    @Test
    void updateComment_shouldReturnUnprocessableEntityStatus_whenTitleIsNull() throws Exception {
        editCommentDTO.setTitle(null);

        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.message").value("Field should contains title!"));
    }

    @Test
    void updateComment_shouldReturnNotFoundStatus_whenCommentNotFound() throws Exception {
        editCommentDTO.setId(100L);

        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Comment with Id " + editCommentDTO.getId() + " not found!"));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void getCommentById_shouldReturnOkStatus(Long id) throws Exception {

        mockMvc.perform(get("/api/comments/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getCommentById_shouldReturnNotFoundStatus_whenCommentNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(get("/api/comments/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Comment with Id " + id + " not found!"));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void removeCommentById_shouldReturnNoContentStatus(Long id) throws Exception {
        mockMvc.perform(delete("/api/comments/"  + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void removeCommentById_shouldReturnNotFoundStatus_whenCommentNotFound() throws Exception {

        Long id = 100L;

        mockMvc.perform(delete("/api/comments/"  + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
