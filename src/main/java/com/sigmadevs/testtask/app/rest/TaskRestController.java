package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.TaskDTO;
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
    public TaskDTO createTask(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }
    @PutMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO);
    }
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }
    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTaskById(@PathVariable("id") Long id) {
        taskService.removeTaskById(id);
    }

}
