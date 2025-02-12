package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.RoomDto;
import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.entity.*;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.RoomRepository;
import com.sigmadevs.testtask.app.repository.UserResultRepository;
import com.sigmadevs.testtask.app.service.RoomService;
import com.sigmadevs.testtask.app.service.TaskService;
import com.sigmadevs.testtask.app.service.TimerService;
import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserMapper userMapper;

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
        List<MessageDto> messageDtos;
        try {

            messageDtos = sessions.get(id).getMessages();
        }catch (Exception e){
            messageDtos = new ArrayList<>();
        }
        if (room != null) {
            return ResponseEntity.ok(new RoomGetDto(room.getId(), room.getTitle(),room.getIsActive(),questMapper.
                    toDTO(room.getQuest()),room.getUsername(),messageDtos,users.get(id)));
        }else return ResponseEntity.badRequest().body(null);

    }


    @MessageMapping("/app/chat/{id}")
    @SendTo("/topic/chat/{id}")
    public MessageDto syncChat(@DestinationVariable("id") Long id, @Payload String message, Principal principal) {
        SessionDto sessionDto = sessions.get(id);
        MessageDto messageDto = new MessageDto(message, principal.getName());
        sessions.put(id, sessionDto);
        return messageDto;
    }

    @MessageMapping("/app/room/{id}")
    @SendTo("/topic/room/{id}")
    public List<UserGetDto> syncChat(@DestinationVariable("id") Long id, Principal principal) {
        Set<String> strings = users.get(id);
        List<String> list = new ArrayList<>(strings);
        List<UserGetDto> list1 = list.stream().map(userService::findByUsername).map(e -> userMapper.userToUserGetDto(e)).toList();
        return list1;
    }
//    @MessageMapping("/app/state/{id}")
//    @SendTo("/topic/state/{id}")
//    public List<TaskAnswDto> syncState(@DestinationVariable("id") Long id, @Payload(required = false) List<TaskAnswDto> taskAnswDtos, Principal principal) {
//        SessionDto sessionDto = sessions.get(id);
//        List<TaskAnswDto> response;
//        if(taskAnswDtos!=null){
//            sessionDto.setTasks(taskAnswDtos);
//            sessions.put(id, sessionDto);
//            response = taskAnswDtos;
//        }else {
//            response = sessionDto.getTasks();
//        }
//        return response;
//    }

}