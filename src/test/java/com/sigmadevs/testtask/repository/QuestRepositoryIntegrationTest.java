package com.sigmadevs.testtask.repository;

import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
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
import static org.junit.Assert.assertTrue;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql" , "/sql/insert_comments.sql"})
public class QuestRepositoryIntegrationTest {
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
    CommentRepository commentRepository;
    Quest quest;
    @BeforeEach
    void setUp() {

        quest = Quest.builder()
                .title("Test title")
                .build();
    }

    @Test
    void save_shouldPersistQuest() {
        questRepository.save(quest);
        assertEquals(6, questRepository.count());
    }

    @Test
    void save_shouldThrowException_whenTitleIsNull() {
        quest.setTitle(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> questRepository.save(quest));
    }
    @Test
    void findAll_shouldReturnQuestList() {
        List<Quest> questList = questRepository.findAll();
        assertNotNull(questList);
        assertFalse(questList.isEmpty());
        assertEquals(5, questList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void findById_shouldReturnQuest(long id) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        assertTrue(optionalQuest.isPresent());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenQuestNotFound() {
        long id = 100L;
        Optional<Quest> optionalQuest = questRepository.findById(id);
        assertTrue(optionalQuest.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void deleteById_shouldRemoveQuest(long id) {
        questRepository.deleteById(id);
        assertEquals(4, questRepository.count());
        assertNotEquals(10,  commentRepository.count());
    }

}
