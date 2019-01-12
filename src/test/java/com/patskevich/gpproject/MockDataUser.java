package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.UserDto;
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

    public static UserDto getUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setNickname("nickname");
        userDto.setRole("ROLE_USER");
        userDto.setRoom(null);
        return userDto;
    }
}