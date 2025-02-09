package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aspectj.bridge.IMessage;

import java.sql.Time;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestDTO {

    @NotNull(message = "Field should contains Id!")
    private Long id;
    @NotNull(message = "Field should contains title!")
    private String title;
    private String description;
    private String image;
    private Time timeLimit;

}
