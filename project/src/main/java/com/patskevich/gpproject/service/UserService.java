package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.NicknameChangeHistoryConverter;
import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.NicknameChangeHistoryDto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.dto.UserDto.*;
import com.patskevich.gpproject.entity.NicknameChangeHistory;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.NicknameChangeHistoryRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserConverter userConverter;
    private final NicknameChangeHistoryRepository nicknameChangeHistoryRepository;
    private final NicknameChangeHistoryConverter nicknameChangeHistoryConverter;

    public String createUser(final CreateUserDto createUserDto) {
        if (!userRepository.existsByName(createUserDto.getName())) {
            userRepository.save(userConverter.convertToDbo(createUserDto));
            return "Пользователь "+createUserDto.getName()+" был создан!";
        } else
            return "Пользователь "+createUserDto.getName()+" уже существует!";
    }

    public String updateUser(final UpdateUserDto updateUserDto) {
        if (!userRepository.existsByNickname(updateUserDto.getNewNickname())) {
            User user = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            NicknameChangeHistory nicknameChangeHistory = new NicknameChangeHistory();
            nicknameChangeHistory.setNewNickname(updateUserDto.getNewNickname());
            nicknameChangeHistory.setOldNickname(user.getNickname());
            nicknameChangeHistory.setUserId(user.getId());
            nicknameChangeHistory.setDate(new Date());
            user.setNickname(updateUserDto.getNewNickname());
            user.setPassword(updateUserDto.getNewPassword());
            nicknameChangeHistoryRepository.save(nicknameChangeHistory);
            userRepository.save(user);
            return "Данные пользователя "+SecurityContextHolder.getContext().getAuthentication().getName()+" были изменены!";
        } else
            return "Пользователь с именем "+updateUserDto.getNewNickname()+" уже существует!";
    }

    public String updateUserNameAdmin(final String newName, final Long id) {
        User user = userRepository.getById(id);
        if (user.getName().equals(newName)) {
            return "Введенные данные совпадают со старыми";
        }
        User findByName = userRepository.findByName(newName);
        if (findByName != null && !findByName.getId().equals(user.getId())) {
            return "Пользователь с логином "+newName+" уже существует!";
        }  else {
            user.setName(newName);
            userRepository.save(user);
            return "Данные успешно изменены";
        }
    }

    public String updateUserNicknameAdmin(final String newNickname, final Long id) {
        User user = userRepository.getById(id);
        if (user.getNickname().equals(newNickname)) {
            return "Введенные данные совпадают со старыми";
        }
        User findByNickname = userRepository.findByNickname(newNickname);
        if (findByNickname != null && !findByNickname.getId().equals(user.getId())) {
            return "Пользователь с никнеймом "+newNickname+" уже существует!";
        }  else {
            NicknameChangeHistory nicknameChangeHistory = new NicknameChangeHistory();
            nicknameChangeHistory.setNewNickname(newNickname);
            nicknameChangeHistory.setOldNickname(user.getNickname());
            nicknameChangeHistory.setUserId(user.getId());
            nicknameChangeHistory.setDate(new Date());
            user.setNickname(newNickname);
            nicknameChangeHistoryRepository.save(nicknameChangeHistory);
            userRepository.save(user);
            return "Данные успешно изменены";
        }
    }

    public List<NicknameChangeHistoryDto> getHistory() {
        return  nicknameChangeHistoryRepository.findAll().stream()
                .map(nicknameChangeHistoryConverter::convertToDto).collect(Collectors.toList());
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
