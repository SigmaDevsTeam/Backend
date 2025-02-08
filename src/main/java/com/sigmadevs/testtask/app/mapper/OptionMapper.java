package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    @Mapping(source = "taskDTO", target = "task")
    Option toEntity(OptionDTO optionDTO);
    @Mapping(source = "task", target = "taskDTO")
    OptionDTO toDTO(Option option);
    @Mapping(source = "task", target = "taskDTO")
    List<OptionDTO> toDTOList(List<Option> options);

}
