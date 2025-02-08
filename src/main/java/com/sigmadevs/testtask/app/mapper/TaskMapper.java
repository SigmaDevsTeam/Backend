package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "questDTO", target = "quest")
    @Mapping(target = "options", ignore = true)
    Task toEntity(TaskDTO taskDTO);
    @Mapping(source = "quest", target = "questDTO")
    @Mapping(target = "options", ignore = true)
    TaskDTO toDTO(Task task);
    @Mapping(source = "quest", target = "questDTO")
    @Mapping(target = "options", ignore = true)
    List<TaskDTO> toDTOList(List<Task> tasks);

}