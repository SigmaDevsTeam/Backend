package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CreateOptionDTO;
import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createOptionDTO.title", target = "title")
    @Mapping(target = "task", source = "task")
    Option toEntity(CreateOptionDTO createOptionDTO, Task task);
    @Mapping(source = "task", target = "taskDTO")
//    @Mapping(source = "options", target = "options")
    OptionDTO toDTO(Option option);
    @Mapping(source = "task", target = "taskDTO")
    List<OptionDTO> toDTOList(List<Option> options);

}
