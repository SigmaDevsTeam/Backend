package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CreateTaskDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createTaskDTO.title", target = "title")
    @Mapping(target = "quest", source = "quest")
    Task toEntity(CreateTaskDTO createTaskDTO, Quest quest);
    @Mapping(source = "quest", target = "questDTO")
    TaskDTO toDTO(Task task);
    @Mapping(source = "quest", target = "questDTO")
    List<TaskDTO> toDTOList(List<Task> tasks);

}