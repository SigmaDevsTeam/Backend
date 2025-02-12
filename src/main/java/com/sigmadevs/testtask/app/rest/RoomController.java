package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.RoomDto;
import com.sigmadevs.testtask.app.entity.*;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.RoomRepository;
import com.sigmadevs.testtask.app.repository.UserResultRepository;
import com.sigmadevs.testtask.app.service.RoomService;
import com.sigmadevs.testtask.app.service.TaskService;
import com.sigmadevs.testtask.app.service.TimerService;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final TimerService timerService;
    private final TaskService taskService;
    private final UserResultRepository resultRepository;
    private final QuestMapper questMapper;

    private final Map<Long, SessionDto> sessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> users = new ConcurrentHashMap<>();

    @GetMapping("/rooms/{id}")
    public ResponseEntity<List<RoomDto>> getRooms(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findAllById(id));
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<RoomDto> createRoom(@RequestBody String title, @PathVariable Long id, Principal principal) {
//        room.setUsername(principal.getName());
        RoomDto room = roomService.createRoom(title, id,principal.getName());
        sessions.put(room.getId(), new SessionDto());
        if(users.get(room.getId())==null) {
            HashSet<String> objects = new HashSet<>();
            objects.add(principal.getName());
            users.put(room.getId(), objects);
        }else {
            Set<String> strings = users.get(room.getId());
            strings.add(principal.getName());
            users.put(room.getId(), strings);
        }
        return ResponseEntity.ok(room);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<RoomGetDto> getRoom(Principal principal, @PathVariable Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        if(users.get(id)==null) {
            HashSet<String> objects = new HashSet<>();
            objects.add(principal.getName());
            users.put(id, objects);
        }else {
            Set<String> strings = users.get(id);
            strings.add(principal.getName());
            users.put(id, strings);
        }

        if (room != null) {
            return ResponseEntity.ok(new RoomGetDto(room.getId(), room.getTitle(),room.getIsActive(),questMapper.
                    toDTO(room.getQuest()),room.getUsername(),sessions.get(id).getMessages(),users.get(id)));
        }else return ResponseEntity.badRequest().body(null);

    }
//    @PostMapping("/room/leave/{id}")
//    public ResponseEntity<String> joinRoom(Principal principal, @PathVariable Long id) {
//        Room room = roomRepository.findById(id).orElseThrow();
//        if (!room.getIsActive()) {
//            User user = userService.findByUsername(principal.getName());
//            Set<Long> longs = users.get(id);
//            if (longs!=null){
//                longs.add(user.getId());
//                users.put(id, longs);
//            }else{
//                Set<Long> newLongs = new HashSet<>();
//                newLongs.add(user.getId());
//                users.put(id,newLongs );
//            }
//            return ResponseEntity.ok("User " + principal.getName() + " joined room " + room.getTitle());
//        } else return ResponseEntity.badRequest().body("Room " + room.getTitle() + " is already in use");
//    }

//    @PostMapping("/room/start/{id}")
//    public ResponseEntity<String> startRoom(@PathVariable Long id, @RequestBody LocalDateTime expiredAt,Principal principal) {
//
//        Room room = roomRepository.findById(id).orElseThrow();
//        room.setIsActive(true);
//        Long id1 = room.getQuest().getId();
//        roomRepository.save(room);
//        sessions.put(id, new SessionDto());
//        System.out.println(ChronoUnit.SECONDS.between(LocalDateTime.now(), expiredAt));
//        System.out.println(ChronoUnit.MINUTES.between(LocalDateTime.now(), expiredAt));
//        timerService.scheduleTask(new Runnable() {
//            @Override
//            @Transactional
//            public void run() {
//                SessionDto sessionDto = sessions.get(id);
//                if(sessionDto!=null){
//                    Set<Long> longs = users.get(id);
//                    longs.stream().forEach(user->{
//                        User byId = userService.findById(user);
//                        List<Task> tasks = sessionDto.getQuest().getTasks();
//                        List<TaskAnswDto> tasks1 = sessionDto.getTasks();
//                        int result=0;
//                        for(Task task:tasks){
//                            Long id2 = task.getId();
//                            TaskAnswDto taskAnswDto = tasks1.stream().filter(e -> e.getId().equals(id2)).findFirst().orElse(null);
//                            if (taskAnswDto!=null){
//                                if(taskAnswDto.getOpenAnswer()!=null){
//                                    boolean equals = taskAnswDto.getOpenAnswer().equals(task.getOpenAnswer());
//                                    if(equals){
//                                        result++;
//                                    }
//                                }else {
//                                    if (task.getOptions()!=null){
//                                        Option option = task.getOptions().stream().filter(Option::getIsTrue).findFirst().orElse(null);
//                                        if(option!=null){
//                                            boolean equals = taskAnswDto.getOptionId().equals(option.getId());
//                                            if(equals){
//                                                result++;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        resultRepository.save(new UserResult(null,byId,sessionDto.getQuest(),result));
//                    });
//
//
//                    sessions.remove(id);
//                    users.remove(id);
//                    roomRepository.deleteById(id);
//                }
//            }
//        }, ChronoUnit.SECONDS.between(LocalDateTime.now(), expiredAt), TimeUnit.SECONDS);
//        syncState(id,taskService.getTasksById(id1).stream().map(e->new TaskAnswDto(e.getId(),null,null)).toList(),principal);
//        return ResponseEntity.ok(expiredAt.toString());
//    }
//
//    @PostMapping("/rooms/stop/{id}")
//    public ResponseEntity<String> stopRoom(@PathVariable Long id,Principal principal) {
//        SessionDto sessionDto = sessions.get(id);
//        if(sessionDto!=null){
//            Set<Long> longs = users.get(id);
//            longs.stream().forEach(user->{
//                User byId = userService.findById(user);
//                List<Task> tasks = sessionDto.getQuest().getTasks();
//                List<TaskAnswDto> tasks1 = sessionDto.getTasks();
//                int result=0;
//                for(Task task:tasks){
//                    Long id2 = task.getId();
//                    TaskAnswDto taskAnswDto = tasks1.stream().filter(e -> e.getId().equals(id2)).findFirst().orElse(null);
//                    if (taskAnswDto!=null){
//                        if(taskAnswDto.getOpenAnswer()!=null){
//                            boolean equals = taskAnswDto.getOpenAnswer().equals(task.getOpenAnswer());
//                            if(equals){
//                                result++;
//                            }
//                        }else {
//                            if (task.getOptions()!=null){
//                                Option option = task.getOptions().stream().filter(Option::getIsTrue).findFirst().orElse(null);
//                                if(option!=null){
//                                    boolean equals = taskAnswDto.getOptionId().equals(option.getId());
//                                    if(equals){
//                                        result++;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                resultRepository.save(new UserResult(null,byId,sessionDto.getQuest(),result));
//            });
//
//
//            sessions.remove(id);
//            users.remove(id);
//            roomRepository.deleteById(id);
//        }
//        return ResponseEntity.ok("stopped");
//    }

    @MessageMapping("/app/chat/{id}")
    @SendTo("/topic/chat/{id}")
    public MessageDto syncChat(@DestinationVariable("id") Long id, @Payload String message, Principal principal) {
        SessionDto sessionDto = sessions.get(id);
        MessageDto messageDto = new MessageDto(message, principal.getName());
        sessionDto.getMessages().add(messageDto);
        return messageDto;
    }

    @MessageMapping("/app/state/{id}")
    @SendTo("/topic/state/{id}")
    public List<TaskAnswDto> syncState(@DestinationVariable("id") Long id, @Payload(required = false) List<TaskAnswDto> taskAnswDtos, Principal principal) {
        SessionDto sessionDto = sessions.get(id);
        List<TaskAnswDto> response;
        if(taskAnswDtos!=null){
            sessionDto.setTasks(taskAnswDtos);
            sessions.put(id, sessionDto);
            response = taskAnswDtos;
        }else {
            response = sessionDto.getTasks();
        }
        return response;
    }

}