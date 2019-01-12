package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserConverter {

    public UserDto convertToDto(final User user) {
        final UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setNickname(user.getNickname());
        userDto.setRoom(user.getRoom().getName());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public User convertToDbo(final CreateUserDtoUi createUserDtoUi) {
        final User user = new User();
        user.setLogin(createUserDtoUi.getLogin());
        user.setNickname(createUserDtoUi.getNickname());
        user.setPassword(createUserDtoUi.getPassword());
        user.setRoom(createUserDtoUi.getRoom());
        user.setRole(createUserDtoUi.getRole());
        return user;
    }
}
