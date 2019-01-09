package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.UserDto.CreateUserDto;
import com.patskevich.gpproject.dto.UserDto.UpdateUserDto;
import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.dto.UserDto.UserNameDto;
import com.patskevich.gpproject.entity.User;

public class MockDataUser {

    public static User getUser() {
        final User user = new User();
        user.setId(new Long(1));
        user.setLogin("login");
        user.setPassword("password");
        user.setNickname("nickname");
        user.setRoom(null);
        user.setRole("ROLE_USER");
        return user;
    }

    public static CreateUserDto getCreateUserDto() {
        final CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setLogin("login");
        createUserDto.setPassword("password");
        return createUserDto;
    }

    public static UpdateUserDto getUpdateUserDto() {
        final UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setNewNickname("new nickname");
        return updateUserDto;
    }

    public static UserNameDto getUserNameDto() {
        final UserNameDto userNameDto = new UserNameDto();
        userNameDto.setName("name");
        return userNameDto;
    }

    public static UserDto getUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setNickname("nickname");
        userDto.setRole("ROLE_USER");
        userDto.setRoom(null);
        return userDto;
    }
}