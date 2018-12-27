package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.UserDto.*;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserConverter userConverter;

    public String createUser(final CreateUserDto createUserDto) {
        if (!userRepository.existsByName(createUserDto.getName())) {
            userRepository.save(userConverter.convertToDbo(createUserDto));
            return "Пользователь "+createUserDto.getName()+" был создан!";
        } else
            return "Пользователь "+createUserDto.getName()+" уже существует!";
    }

    public String updateUser(final UpdateUserDto updateUserDto) {
        if (userRepository.existsByName(updateUserDto.getNewName())) {
            User user = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setName(updateUserDto.getNewName());
            user.setPassword(updateUserDto.getNewPassword());
            userRepository.save(user);
            return "Данные пользователя "+SecurityContextHolder.getContext().getAuthentication().getName()+" были изменены!";
        } else
            return "Пользователь с именем "+updateUserDto.getNewName()+" уже существует!";
    }

    public List<UserDto> getUserList() {
        return userRepository.findAll().stream().map(userConverter::convertToDto).collect(Collectors.toList());
    }

    public String deleteUser(final UserNameDto nameUserDto) {
        if (userRepository.existsByName(nameUserDto.getName())) {
            userRepository.delete(userRepository.findByName(nameUserDto.getName()));
            return "Пользователь "+nameUserDto.getName()+" был удален!";
        } else
            return "Пользователя "+nameUserDto.getName()+" не существует!";
    }

    public UserDto getUser(final String name) {
        return userConverter.convertToDto(userRepository.findByName(name));
    }

    public String changeRoom(final UserChangeRoom userChangeRoom) {
        if (roomRepository.existsByName(userChangeRoom.getRoom())) {
            User user = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setRoom(roomRepository.findByName(userChangeRoom.getRoom()));
            userRepository.save(user);
            return "Пользователь "+SecurityContextHolder.getContext().getAuthentication().getName()+" переместился в комнату "+userChangeRoom.getRoom()+"!";
        } else
            return "Комнаты с именем "+userChangeRoom.getRoom()+" не существует!";
    }

    public String changeRoom(final String name) {
        if (roomRepository.existsByName(name)) {
            User user = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setRoom(roomRepository.findByName(name));
            userRepository.save(user);
            return "Пользователь "+SecurityContextHolder.getContext().getAuthentication().getName()+" переместился в комнату "+name+"!";
        } else
            return "Комнаты с именем "+name+" не существует!";
    }

    public String changeRoleUser(final UserNameDto userNameDto) {
        if (userRepository.existsByName(userNameDto.getName())) {
            User user = userRepository.findByName(userNameDto.getName());
            if (user.getRole().equals("ROLE_USER")) user.setRole("ROLE_ADMIN");
                    else user.setRole("ROLE_USER");
            userRepository.save(user);
            return "Пользователь "+userNameDto.getName()+" изменил роль на "+user.getRole();
        } else
            return "Пользователя с именем "+userNameDto.getName()+" не существует!";
    }
}
