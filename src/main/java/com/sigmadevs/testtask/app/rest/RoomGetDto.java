package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomGetDto {
    private Long id;
    private String title;
    private Boolean isActive;
    private QuestDTO quest;
    private String username;
    private List<MessageDto> messages;
    private Set<String> users;
}
