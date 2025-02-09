package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MainUserMapper {
    @Mapping(target = "quests", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);
    @Mapping(target = "quests", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserDTO toDTO(User user);

}
