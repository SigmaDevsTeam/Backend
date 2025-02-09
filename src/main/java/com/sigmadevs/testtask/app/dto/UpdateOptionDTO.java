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
public class UpdateOptionDTO {

    @NotNull(message = "Field should contains Id!")
    private Long id;

    @NotBlank(message = "Field should contains title!")
    private String title;

    @NotNull(message = "Option should be true or false!")
    private Boolean isTrue;

}
