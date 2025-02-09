package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestDTO {

    @NotBlank(message = "Field should contains title!")
    private String title;

    private String description;

    private String image;

    private Time timeLimit;

    @NotNull(message = "Field should contains user Id!")
    private Long userId;


}
