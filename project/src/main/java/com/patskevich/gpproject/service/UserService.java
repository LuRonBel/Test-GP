package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.UserDto.*;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserConverter userConverter;

    public String createUser(final CreateUserDto createUserDto) {
        if (!userRepository.existsByLogin(createUserDto.getLogin())) {
                createUserDto.setPassword(encoder().encode(createUserDto.getPassword()));
                userRepository.save(userConverter.convertToDbo(createUserDto));
            return "Пользователь "+createUserDto.getLogin()+" был создан!";
        } else
            return "Пользователь "+createUserDto.getLogin()+" уже существует!";
    }

    public List<UserDto> getUserList() {
        return userRepository.findAll().stream().map(userConverter::convertToDto).collect(Collectors.toList());
    }

    public String deleteUser(final UserNameDto nameUserDto) {
        if (nameUserDto.getName().equals("root")) return "Этого пользователя удалить нельзя!";
        else if (userRepository.existsByLogin(nameUserDto.getName())) {
                 userRepository.delete(userRepository.findByLogin(nameUserDto.getName()));
                 return "Пользователь "+nameUserDto.getName()+" был удален!";
             } else
                 return "Пользователя "+nameUserDto.getName()+" не существует!";
    }

    public User getUser(final String name) {
        return userRepository.findByLogin(name);
    }

    public String changeRoom(final NameRoomDto nameRoomDto, final String login) {
        if (roomRepository.existsByName(nameRoomDto.getName())) {
            final User user = userRepository.findByLogin(login);
            user.setRoom(roomRepository.findByName(nameRoomDto.getName()));
            userRepository.save(user);
            return "Пользователь "+login+" переместился в комнату "+nameRoomDto.getName()+"!";
        } else
            return "Комнаты с именем "+nameRoomDto.getName()+" не существует!";
    }

    public String changeRoom(final String name, final String login) {
        if (roomRepository.existsByName(name)) {
            final User user = userRepository.findByLogin(login);
            user.setRoom(roomRepository.findByName(name));
            userRepository.save(user);
            return "Пользователь "+login+" переместился в комнату "+name+"!";
        } else
            return "Комнаты с именем "+name+" не существует!";
    }

    public String changeRoleUser(final UserNameDto userNameDto) {
        if (userNameDto.getName().equals("root")) return "Нельзя изменить этого пользователя";
            else if (userRepository.existsByLogin(userNameDto.getName())) {
                final User user = userRepository.findByLogin(userNameDto.getName());
                if (user.getRole().equals("ROLE_USER")) user.setRole("ROLE_ADMIN");
                else user.setRole("ROLE_USER");
                userRepository.save(user);
                return "Пользователь "+userNameDto.getName()+" изменил роль на "+user.getRole();
            } else return "Пользователя с именем "+userNameDto.getName()+" не существует!";
    }

    public List<String> getNameUserList(){
        final List<User> userList = userRepository.findAll();
        final List<String> nameUserList = new ArrayList<>();
        for (User user: userList) {
            nameUserList.add(user.getLogin());
        }
        return nameUserList;
    }

    @Autowired
    private PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
