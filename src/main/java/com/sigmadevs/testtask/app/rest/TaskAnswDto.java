package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAnswDto {
    private Long id;
    private String openAnswer;
    private Long optionId;
}
