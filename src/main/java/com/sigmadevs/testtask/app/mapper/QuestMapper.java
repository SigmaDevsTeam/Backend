package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MainUserMapper.class })
public interface QuestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createQuestDTO.title", target = "title")
    @Mapping(source = "createQuestDTO.image", target = "image")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "taskCount", ignore = true)
    @Mapping(target = "usersRated", ignore = true)
    @Mapping(target = "rating", ignore = true)
    Quest toEntity(CreateQuestDTO createQuestDTO, User user);
    @Mapping(source = "user", target = "userDTO")
    QuestDTO toDTO(Quest quest);
    @Mapping(source = "userDTO", target = "user")
    List<QuestDTO> toDTOList(List<Quest> quests);

}
