package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDTO {
    @NotBlank(message = "Field should contains title!")
    private String title;
    @NotNull(message = "Field should contains user Id!")
    private Long userId;
    @NotNull(message = "Field should contains quest Id!")
    private Long questId;

}
