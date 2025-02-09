package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.dto.UpdateTaskDTO;
import com.sigmadevs.testtask.app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskRestController {

    private final TaskService taskService;
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO) {
        return taskService.createTask(createTaskDTO);
    }
    @PutMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@RequestBody @Valid UpdateTaskDTO updateTaskDTO) {
        return taskService.updateTask(updateTaskDTO);
    }
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id);
    }
    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTaskById(@PathVariable("id") Long id) {
        taskService.removeTaskById(id);
    }

}
