package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.dto.UpdateTaskDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.exception.TaskNotFoundException;
import com.sigmadevs.testtask.app.mapper.TaskMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
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
    private final QuestRepository questRepository;
    private final TaskMapper taskMapper;

    public TaskDTO createTask(CreateTaskDTO createTaskDTO) {
        log.info("Creating task with title: {}", createTaskDTO.getTitle());

        Quest quest = questRepository.findById(createTaskDTO.getQuestId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", createTaskDTO.getQuestId());
                    return new QuestNotFoundException("Quest with Id " + createTaskDTO.getQuestId() + " not found!");
                });

        Task task = taskRepository.save(taskMapper.toEntity(createTaskDTO, quest));
        log.info("Task created with ID: {}", task.getId());
        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskDTO updateTask(UpdateTaskDTO updateTaskDTO) {
        log.info("Updating task with ID: {}", updateTaskDTO.getId());

        Task task = taskRepository.findById(updateTaskDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Task with ID {} not found!", updateTaskDTO.getId());
                    return new TaskNotFoundException("Task with Id " + updateTaskDTO.getId() + " not found!");
                });

        task.setTitle(updateTaskDTO.getTitle());
        task.setAudio(updateTaskDTO.getAudio());
        task.setVideo(updateTaskDTO.getVideo());
        task.setImage(updateTaskDTO.getImage());
        task.setOpenAnswer(updateTaskDTO.getOpenAnswer());

        log.info("Task with ID {} updated successfully", task.getId());
        return taskMapper.toDTO(task);
    }
    public List<TaskDTO> getAllTasks() {
        log.info("Fetching all tasks...");
        List<Task> tasks = taskRepository.findAll();
        log.info("Retrieved {} tasks", tasks.size());
        return taskMapper.toDTOList(tasks);
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
