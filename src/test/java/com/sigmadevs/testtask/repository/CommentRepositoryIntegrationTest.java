package com.sigmadevs.testtask.repository;

import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.security.entity.Role;
import com.sigmadevs.testtask.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql" , "/sql/insert_comments.sql"})
public class CommentRepositoryIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    QuestRepository questRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;
    Comment comment;
    @BeforeEach
    void setUp() {

        Quest quest = Quest.builder()
                .id(1L)
                .title("Test title")
                .build();

        User user = User.builder()
                .id(1L)
                .email("Test email")
                .username("Test username")
                .password("Test password")
                .image("image.jpg")
                .role(Role.USER)
                .build();

        comment = Comment.builder()
                .title("Test title")
                .quest(quest)
                .user(user)
                .build();
    }

    @Test
    void save_shouldPersisComment() {
        commentRepository.save(comment);
        assertEquals(11, commentRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(5, questRepository.count());
    }

    @Test
    void save_shouldThrowException_whenTitleIsNull() {
        comment.setTitle(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void save_shouldThrowException_whenUserIsNull() {
        comment.setUser(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void save_shouldThrowException_whenQuestIsNull() {
        comment.setQuest(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    void findAll_shouldReturnQuestList() {
        List<Comment> commentList = commentRepository.findAll();
        assertNotNull(commentList);
        assertFalse(commentList.isEmpty());
        assertEquals(10, commentList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void findById_shouldReturnComment(long id) {
        Optional<Comment> commentOption = commentRepository.findById(id);
        assertTrue(commentOption.isPresent());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenCommentNotFound() {
        long id = 100L;
        Optional<Comment> optionalComment = commentRepository.findById(id);
        assertTrue(optionalComment.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void existsById_shouldReturnTrue(long id) {
        boolean isCommentExists = commentRepository.existsById(id);
        assertTrue(isCommentExists);
    }

    @Test
    void existsById_shouldReturnFalse_whenCommentNotFound() {
        long id = 100L;
        boolean isCommentExists = commentRepository.existsById(id);
        assertFalse(isCommentExists);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void deleteById_shouldRemoveComment(long id) {
        commentRepository.deleteById(id);
        assertEquals(9, commentRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(5, questRepository.count());
    }

    @Test
    void deleteById_shouldNotRemoveComment_whenCommentNotFound() {
        long id = 100;
        commentRepository.deleteById(id);
        assertEquals(10, commentRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(5, questRepository.count());
    }

}
