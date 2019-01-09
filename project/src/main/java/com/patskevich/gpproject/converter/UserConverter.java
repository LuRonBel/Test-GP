package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.UserDto.CreateUserDto;
import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserConverter {

    private final RoomRepository roomRepository;

    public UserDto convertToDto(final User user) {
        final UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public User convertToDbo(final UserDto userDto) {
        final User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    public User convertToDbo(final CreateUserDto createUserDto) {
        final User user = new User();
        user.setLogin(createUserDto.getName());
        user.setNickname(createUserDto.getNickname());
        user.setPassword(createUserDto.getPassword());
        user.setRoom(roomRepository.findByName("Default room"));
        user.setRole("ROLE_USER");
        return user;
    }
}
