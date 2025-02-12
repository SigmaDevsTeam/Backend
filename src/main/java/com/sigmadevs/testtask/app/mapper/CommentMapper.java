package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.WriteCommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "writeCommentDTO.title", target = "title")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "quest", target = "quest")
    Comment toEntity(WriteCommentDTO writeCommentDTO, User user, Quest quest);
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "quest", target = "questDTO")
    CommentDTO toDTO(Comment comment);

}