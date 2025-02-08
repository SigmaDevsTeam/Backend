package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.TaskNotFoundException;
import com.sigmadevs.testtask.app.mapper.TaskMapper;
import com.sigmadevs.testtask.app.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDTO createTask(TaskDTO taskDTO) {
        log.info("Creating task with title: {}", taskDTO.getTitle());
        Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
        log.info("Task created with ID: {}", task.getId());
        return taskMapper.toDTO(task);
    }

    public List<TaskDTO> getAllTasks() {
        log.info("Fetching all tasks...");
        List<Task> tasks = taskRepository.findAll();
        log.info("Retrieved {} tasks", tasks.size());
        return taskMapper.toDTOList(tasks);
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO taskDTO) {
        log.info("Updating task with ID: {}", taskDTO.getId());

        Task task = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Task with ID {} not found!", taskDTO.getId());
                    return new TaskNotFoundException("Task with Id " + taskDTO.getId() + " not found!");
                });

        task.setTitle(taskDTO.getTitle());
        task.setAudio(taskDTO.getAudio());
        task.setVideo(taskDTO.getVideo());
        task.setImage(taskDTO.getImage());
        task.setOpenAnswer(taskDTO.getOpenAnswer());

        log.info("Task with ID {} updated successfully", task.getId());
        return taskMapper.toDTO(task);
    }

    public TaskDTO getTaskById(long id) {
        log.info("Fetching task with ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with ID {} not found!", id);
                    return new TaskNotFoundException("Task with Id " + id + " not found!");
                });

        log.info("Retrieved task with ID {}: {}", task.getId(), task);
        return taskMapper.toDTO(task);
    }

    public void removeTaskById(long id) {
        log.info("Attempting to remove task with ID: {}", id);

        if (!taskRepository.existsById(id)) {
            log.warn("Task with ID {} not found!", id);
            throw new TaskNotFoundException("Task with Id " + id + " not found!");
        }

        taskRepository.deleteById(id);
        log.info("Task with ID {} removed successfully", id);
    }
}
