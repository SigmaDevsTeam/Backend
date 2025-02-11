package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDTO {

    @NotBlank(message = "Field should contains title!")
    private String title;

    private String audio;

    private String image;

    private String video;

    private String openAnswer;

    List<CreateOptionDTO> options;

    @NotNull(message = "Field should contains quest Id!")
    private Long questId;

}
