package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "userDTO", target = "user")
    @Mapping(source = "questDTO", target = "quest")
    Comment toEntity(CommentDTO commentDTO);
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "quest", target = "questDTO")
    CommentDTO toDTO(Comment comment);

}
