package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.TaskNotFoundException;
import com.sigmadevs.testtask.app.mapper.TaskMapper;
import com.sigmadevs.testtask.app.repository.TaskRepository;
import com.sigmadevs.testtask.app.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    Task task;
    TaskDTO taskDTO;

    @BeforeEach
    void setUp() {

        taskDTO = TaskDTO.builder()
                .title("Test title")
                .build();

        task = Task.builder()
                .title("Test title")
                .build();

    }


    @Test
    void createTask_shouldReturnTaskDTO() {

        when(taskMapper.toEntity(any(TaskDTO.class)))
                .thenReturn(task);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        when(taskMapper.toDTO(any(Task.class)))
                .thenReturn(taskDTO);

        TaskDTO actualTaskDTO = taskService.createTask(taskDTO);

        assertNotNull(actualTaskDTO);
        assertEquals(actualTaskDTO, taskDTO);

        verify(taskMapper).toEntity(any(TaskDTO.class));
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDTO(any(Task.class));
    }

    @Test
    void getAllTasks_shouldReturnTaskDTOList() {

        when(taskRepository.findAll())
                .thenReturn(List.of(task));

        when(taskMapper.toDTOList(any(List.class)))
                .thenReturn(List.of(taskDTO));

        List<TaskDTO> taskDTOS = taskService.getAllTasks();

        assertNotNull(taskDTOS);
        assertFalse(taskDTOS.isEmpty());
        assertEquals(1 , taskDTOS.size());

        verify(taskRepository).findAll();
        verify(taskMapper).toDTOList(any(List.class));
    }

    @Test
    void updateTask_shouldReturnTaskDTO() {

        taskDTO.setTitle("Test title 2");

        when(taskRepository.findById(taskDTO.getId()))
                .thenReturn(Optional.of(task));

        when(taskMapper.toDTO(any(Task.class)))
                .thenReturn(taskDTO);

        TaskDTO actualTaskDTO = taskService.updateTask(taskDTO);

        assertNotNull(actualTaskDTO);
        assertEquals(actualTaskDTO, taskDTO);

        verify(taskRepository).findById(taskDTO.getId());
        verify(taskMapper).toDTO(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {

        when(taskRepository.findById(taskDTO.getId()))
                .thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskDTO));

        assertEquals("Task with Id " + taskDTO.getId() + " not found!", exception.getMessage());

        verify(taskRepository).findById(taskDTO.getId());
        verify(taskMapper,never()).toDTO(any(Task.class));
    }

    @Test
    void getTaskById_shouldReturnTaskDTO() {

        long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        when(taskMapper.toDTO(any(Task.class)))
                .thenReturn(taskDTO);

        TaskDTO actualTask = taskService.getTaskById(id);

        assertNotNull(actualTask);
        assertEquals(taskDTO, actualTask);

        verify(taskRepository).findById(id);
        verify(taskMapper).toDTO(any(Task.class));
    }

    @Test
    void getTaskById_shouldThrowException_whenTaskNotFound() {

        long id = 100L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(id));

        assertEquals("Task with Id " + id + " not found!", exception.getMessage());

        verify(taskRepository).findById(id);
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    void removeTaskById_shouldDeleteTask() {

        long id = 1L;

        when(taskRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(taskRepository).deleteById(id);

        taskService.removeTaskById(id);

        verify(taskRepository).existsById(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    void removeTaskById_shouldThrowException_whenTaskNotFound() {

        long id = 100L;

        when(taskRepository.existsById(id))
                .thenReturn(false);

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.removeTaskById(id));

        assertEquals("Task with Id " + id + " not found!", exception.getMessage());

        verify(taskRepository).existsById(id);
        verify(taskRepository, never()).deleteById(id);
    }



}
