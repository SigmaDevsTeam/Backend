package com.sigmadevs.testtask.app.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    private String title;

    private String audio;

    private String video;

    private String image;

    private String openAnswer;

    private QuestDTO questDTO;

    private List<OptionDTO> options;


}
