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
public class OptionDTO {

    private Long id;

    @NotBlank(message = "Field should contains title!")
    private String title;

    @NotNull(message = "Field can not be null!")
    private Boolean isTrue;

    @NotNull(message = "Field can not be null!")
    private TaskDTO taskDTO;

}
