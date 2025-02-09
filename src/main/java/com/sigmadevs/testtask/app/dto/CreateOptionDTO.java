package com.sigmadevs.testtask.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateOptionDTO {

    @NotBlank(message = "Field should contains title!")
    private String title;

    @NotNull(message = "Option should be true or false!")
    private Boolean isTrue;

    @NotNull(message = "Field should contains task Id!")
    private Long taskId;

}
