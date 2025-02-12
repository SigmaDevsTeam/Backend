package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.dto.UpdateTaskDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.exception.TaskNotFoundException;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.app.mapper.OptionMapper;
import com.sigmadevs.testtask.app.mapper.TaskMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.repository.TaskRepository;
import com.sigmadevs.testtask.security.repository.UserRepository;
import com.sigmadevs.testtask.security.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final TaskMapper taskMapper;
    private final ImageService imageService;
    private final OptionService optionService;
    private final OptionMapper optionMapper;

    public ResponseEntity<TaskDTO> createTask(CreateTaskDTO createTaskDTO, MultipartFile image,MultipartFile video,MultipartFile audio,Principal principal) {
        log.info("Creating task with title: {}", createTaskDTO.getTitle());
        User user1 = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new UserNotFoundException(principal.getName()));

        Quest quest = questRepository.findById(createTaskDTO.getQuestId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", createTaskDTO.getQuestId());
                    return new QuestNotFoundException("Quest with Id " + createTaskDTO.getQuestId() + " not found!");
                });
        if(quest.getUser().getUsername().equals(user1.getUsername())) {
            if (image!=null){
                String uploadedImage = imageService.uploadImage("task/image", image);
                createTaskDTO.setImage(uploadedImage);
            }if (video!=null){
                String uploadedVideo = imageService.uploadImage("task/video", video);
                createTaskDTO.setAudio(uploadedVideo);
            }if (audio!=null){
                String uploadedAudio = imageService.uploadImage("task/audio", audio);
                createTaskDTO.setAudio(uploadedAudio);
            }
            Task task = taskRepository.save(taskMapper.toEntity(createTaskDTO, quest));
            if (task.getOptions()!=null){

                optionService.createAllOptions(createTaskDTO.getOptions(),task);
            }
            log.info("Task created with ID: {}", task.getId());
            return ResponseEntity.ok(taskMapper.toDTO(task));
        }else {
           return ResponseEntity.status(403).body(null);
        }
    }

    @Transactional
    public ResponseEntity<TaskDTO> updateTask(CreateTaskDTO updateTaskDTO, MultipartFile image, MultipartFile video, MultipartFile audio, Principal principal,Long id) {
        log.info("Updating task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task with ID {} not found!", id);
                    return new TaskNotFoundException("Task with Id " + id + " not found!");
                });
        if(task.getQuest().getUser().getUsername().equals(principal.getName())) {

            task.setTitle(updateTaskDTO.getTitle()!=null?updateTaskDTO.getTitle():task.getTitle());
            task.setAudio(updateTaskDTO.getAudio()!=null?updateTaskDTO.getAudio():task.getAudio());
            task.setVideo(updateTaskDTO.getVideo()!=null?updateTaskDTO.getVideo():task.getVideo());
            task.setImage(updateTaskDTO.getImage()!=null?updateTaskDTO.getImage():task.getImage());
            if (image!=null) {
                String url = imageService.updateImage(task.getImage(), image);
                task.setImage(url);
            }if (video!=null) {
                String url = imageService.updateImage(task.getVideo(), video);
                task.setVideo(url);
            }if (audio!=null) {
                String url = imageService.updateImage(task.getAudio(), audio);
                task.setAudio(url);
            }
            if (updateTaskDTO.getOpenAnswer()!=null) {

                task.setOpenAnswer(updateTaskDTO.getOpenAnswer());
                if(!task.getOptions().isEmpty()){
                    task.getOptions().clear();
                }
            }
            if (updateTaskDTO.getOptions()!=null) {

                List<Option> list = updateTaskDTO.getOptions().stream().map(e -> optionMapper.toEntity(e, task)).toList();
                task.getOptions().clear();
                task.getOptions().addAll(list);
                if(task.getOpenAnswer()!=null) {
                    task.setOpenAnswer(null);
                }
            }
            log.info("Task with ID {} updated successfully", task.getId());
            return ResponseEntity.ok(taskMapper.toDTO(task));
        }else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

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

    public ResponseEntity<String> removeTaskById(long id, Principal principal) {
        log.info("Attempting to remove task with ID: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with Id " + id + " not found!"));
        if (task.getQuest().getUser().getUsername().equals(principal.getName())) {
            taskRepository.deleteById(id);
            log.info("Task with ID {} removed successfully", id);
            return ResponseEntity.ok("Task with Id " + id + " removed successfully");
        } else {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
    }
    public List<TaskDTO> getTasksById(Long id) {
        List<Task> task = taskRepository.findTasksByQuest_Id(id);
        return task.stream().map(taskMapper::toDTO).toList();
    }
}
