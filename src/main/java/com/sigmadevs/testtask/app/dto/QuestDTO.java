package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Time;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestDTO {

    private Long id;

    @NotBlank(message = "Field should contains title!")
    private String title;

    private String description;

    private String image;

    private Integer taskCount;

    private Time timeLimit;

    private UserDTO userDTO;

    private Integer usersRated;

    private Float rating;

    private List<CommentDTO> comments;

}
