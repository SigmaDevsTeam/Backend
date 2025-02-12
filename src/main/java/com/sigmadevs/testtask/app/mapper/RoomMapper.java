package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.RoomDto;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Room;
import com.sigmadevs.testtask.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface RoomMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "quest",source = "quest")
    @Mapping(target = "title", source = "createQuestDTO.title")
    Room toEntity(RoomDto createQuestDTO, Quest quest);
    @Mapping(source = "quest", target = "quest")
    RoomDto toDTO(Room quest);
}
