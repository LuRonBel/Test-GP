package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.entity.User;

public class MockDataUser {

    public static User getUser() {
        final User user = new User();
        user.setId(1L);
        user.setLogin("login");
        user.setPassword("password");
        user.setNickname("nickname");
        user.setRoom(null);
        user.setRole(RoleEnum.ROLE_USER.toString());
        return user;
    }

    public static UserDto getUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setNickname("nickname");
        userDto.setRole(RoleEnum.ROLE_USER.toString());
        userDto.setRoom(null);
        return userDto;
    }

    public static CreateUserDtoUi getCreateUserDtoUi() {
        final CreateUserDtoUi userDto = new CreateUserDtoUi();
        userDto.setLogin("login");
        userDto.setNickname("nickname");
        userDto.setPassword("password");
        userDto.setRole(RoleEnum.ROLE_USER.toString());
        userDto.setRoom(null);
        return userDto;
    }
}