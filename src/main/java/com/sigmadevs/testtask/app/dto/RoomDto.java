package com.sigmadevs.testtask.app.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private String title;
    private Boolean isActive;
    private QuestDTO quest;
    private String username;

}
