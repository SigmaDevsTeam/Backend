package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long id;

    private String title;

    private UserDTO userDTO;

    private QuestDTO questDTO;

}