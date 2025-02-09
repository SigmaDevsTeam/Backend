package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskDTO {

    @NotNull(message = "Field should contains Id!")
    private Long id;
    @NotBlank(message = "Field should contains title!")
    private String title;
    private String audio;
    private String video;
    private String image;
    private String openAnswer;


}
