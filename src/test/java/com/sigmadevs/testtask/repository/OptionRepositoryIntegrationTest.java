package com.sigmadevs.testtask.repository;


import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.OptionRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.repository.TaskRepository;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql", "/sql/insert_tasks.sql", "/sql/insert_options.sql"})
public class OptionRepositoryIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    QuestRepository questRepository;

    @Autowired
    CommentRepository commentRepository;

    Option option;
    @BeforeEach
    void setUp() {

        Task task = Task.builder()
                .title("Test title")
                .build();

        option = Option.builder()
                .title("Test title")
                .isTrue(true)
                .task(task)
                .build();
    }

    @Test
    void save_shouldPersisOptionAndTask() {
        optionRepository.save(option);
        assertEquals(11, optionRepository.count());
        assertEquals(6, taskRepository.count());
    }

    @Test
    void save_shouldThrowException_whenTitleIsNull() {
        option.setTitle(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> optionRepository.save(option));
    }

    @Test
    void save_shouldThrowException_whenIsTrueNull() {
        option.setIsTrue(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> optionRepository.save(option));
    }

    @Test
    void findAll_shouldReturnOptionList() {
        List<Option> optionList = optionRepository.findAll();
        assertNotNull(optionList);
        assertFalse(optionList.isEmpty());
        assertEquals(10, optionList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void findById_shouldReturnOption(long id) {
        Optional<Option> optionalOption = optionRepository.findById(id);
        assertTrue(optionalOption.isPresent());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenOptionNotFound() {
        long id = 100L;
        Optional<Option> optionalOption = optionRepository.findById(id);
        assertTrue(optionalOption.isEmpty());
    }
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void existsById_shouldReturnTrue(long id) {
        boolean isOptionExists = optionRepository.existsById(id);
        assertTrue(isOptionExists);
    }
    @Test
    void existsById_shouldReturnFalse_whenOptionNotFound() {
        long id = 100L;
        boolean isOptionExists = optionRepository.existsById(id);
        assertFalse(isOptionExists);
    }
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L})
    void deleteById_shouldRemoveOption(long id) {
        optionRepository.deleteById(id);
        assertEquals(9, optionRepository.count());
        assertEquals(5, taskRepository.count());
    }

    @Test
    void deleteById_shouldNotRemoveOption_whenOptionNotFound() {
        long id = 100L;
        optionRepository.deleteById(id);
        assertEquals(10, optionRepository.count());
        assertEquals(5, taskRepository.count());
    }

}
