package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MainUserMapper.class, CommentMapper.class })
public interface QuestMapper {
    @Mapping(source = "userDTO", target = "user")
    @Mapping(target = "comments", ignore = true)
    Quest toEntity(QuestDTO questDTO);
    @Mapping(source = "user", target = "userDTO")
    @Mapping(target = "comments", ignore = true)
    QuestDTO toDTO(Quest quest);
    @Mapping(source = "userDTO", target = "user")
    @Mapping(target = "comments", ignore = true)
    List<QuestDTO> toDTOList(List<Quest> quests);

}
