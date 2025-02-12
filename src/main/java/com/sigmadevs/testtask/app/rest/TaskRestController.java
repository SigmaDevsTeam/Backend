package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.dto.UpdateTaskDTO;
import com.sigmadevs.testtask.app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskRestController {

    private final TaskService taskService;
    @PostMapping(value = "/tasks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createTask(@RequestPart @Valid CreateTaskDTO task, Principal principal,@RequestPart(required = false) MultipartFile image,@RequestPart(required = false) MultipartFile video,@RequestPart(required = false) MultipartFile audio) {
        return taskService.createTask(task,image,video,audio,principal);
    }
    @PutMapping(value = "/tasks/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> updateTask(@RequestPart @Valid CreateTaskDTO task, Principal principal, @RequestPart(required = false) MultipartFile image, @RequestPart(required = false) MultipartFile video, @RequestPart(required = false) MultipartFile audio, @PathVariable Long id) {
        return taskService.updateTask(task,image,video,audio,principal,id);
    }
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getTasksById(@PathVariable("id") Long id) {
        return taskService.getTasksById(id);
    }

    @DeleteMapping("/tasks/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removeTaskById(@PathVariable("id") Long id, Principal principal) {
        return taskService.removeTaskById(id,principal);
    }

}
