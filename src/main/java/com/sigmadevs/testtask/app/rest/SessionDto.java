package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Room;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {
    private Room room;
    private Quest quest;
    private LocalDateTime expiredAt;
    private List<MessageDto> messages;
    private List<TaskAnswDto> tasks;
    private List<UserDTO> users;
}
