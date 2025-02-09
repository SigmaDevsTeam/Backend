package com.sigmadevs.testtask.app.mapper;

import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MainUserMapper {
    UserDTO toDTO(User user);
}
