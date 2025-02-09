package com.sigmadevs.testtask.repository;

import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
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
import static org.junit.Assert.assertTrue;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_users.sql", "/sql/insert_quests.sql", "/sql/insert_tasks.sql"})
public class TaskRepositoryIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    QuestRepository questRepository;
    Task task;
    @BeforeEach
    void setUp() {

        Quest quest = Quest.builder()
                .title("Test quest")
                .build();

        task = Task.builder()
                .quest(quest)
                .title("Test title")
                .build();
    }

    @Test
    void save_shouldPersistTaskAndQuest() {
        taskRepository.save(task);
        assertEquals(6, taskRepository.count());
        assertEquals(6, questRepository.count());
    }

    @Test
    void save_shouldThrowException_whenTitleIsNull() {
        task.setTitle(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> taskRepository.save(task));
    }
    @Test
    void findAll_shouldReturnTaskList() {
        List<Task> taskList = taskRepository.findAll();
        assertNotNull(taskList);
        assertFalse(taskList.isEmpty());
        assertEquals(5, taskList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void findById_shouldReturnTask(long id) {
        Optional<Task> taskOption = taskRepository.findById(id);
        assertTrue(taskOption.isPresent());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenTaskNotFound() {
        long id = 100L;
        Optional<Task> optionalTask = taskRepository.findById(id);
        assertTrue(optionalTask.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L,5L})
    void deleteById_shouldRemoveTask(long id) {
        taskRepository.deleteById(id);
        assertEquals(4, taskRepository.count());
        assertEquals(5, questRepository.count());
    }

}
