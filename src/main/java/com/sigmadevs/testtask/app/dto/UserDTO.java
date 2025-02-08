package com.sigmadevs.testtask.app.dto;

import com.sigmadevs.testtask.security.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private String image;

    private Role role;

    private Integer usersRated;

    private Float rating;

    private List<QuestDTO> quests;

    private List<CommentDTO> comments;

}
