package com.sigmadevs.testtask.security.mapper;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface UserMapper {

    UserGetDto userToUserGetDto(User user);

    User userGetDtoToUser(UserGetDto userGetDto);

    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

}
