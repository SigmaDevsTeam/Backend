package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Field should contains title!")
    private String title;

    private String audio;

    private String video;

    private String image;

    private String openAnswer;

    private QuestDTO questDTO;

    private List<OptionDTO> options;

}
