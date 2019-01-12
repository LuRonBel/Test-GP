package com.patskevich.gpproject.service;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.NicknameChangeHistoryConverter;
import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.NicknameChangeHistory;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.NicknameChangeHistoryRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public String createUser(final String login, final String password) {
        if (!userRepository.existsByLogin(login)) {
                final User user = new User();
                user.setLogin(login);
                user.setNickname(UserDto.NEW_NICKNAME);
                user.setPassword(encoder().encode(password));
                user.setRoom(roomRepository.findByName(RoomDto.DEFAULT_ROOM));
                user.setRole(RoleEnum.ROLE_USER.toString());
                userRepository.save(user);
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("user.exists");
    }

    public String createUser(final CreateUserDtoUi createUserDtoUi) {
        if (!userRepository.existsByLogin(createUserDtoUi.getLogin())) {
            createUserDtoUi.setPassword(encoder().encode(createUserDtoUi.getPassword()));
            userRepository.save(userConverter.convertToDbo(createUserDtoUi));
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("user.exists");
    }

    public String updateUserUi(final CreateUserDtoUi createUserDtoUi, final String login){
        if (!userRepository.existsByLogin(createUserDtoUi.getLogin())){
            final User user = userRepository.findByLogin(login);
            user.setLogin(createUserDtoUi.getLogin());
            user.setNickname(createUserDtoUi.getNickname());
            user.setPassword(encoder().encode(createUserDtoUi.getPassword()));
            user.setRoom(createUserDtoUi.getRoom());
            user.setRole(createUserDtoUi.getRole());
            userRepository.save(user);
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("error.create");
    }

    public String changeNicknameAndPass(final String newNickname, final String newPassword, final String login) {
        final User user = userRepository.findByLogin(login);
        final NicknameChangeHistory nicknameChangeHistory = new NicknameChangeHistory();
        nicknameChangeHistory.setNewNickname(newNickname);
        nicknameChangeHistory.setOldNickname(user.getNickname());
        nicknameChangeHistory.setUserId(user.getId());
        nicknameChangeHistory.setDate(new Date());
        user.setNickname(newNickname);
        user.setPassword(encoder().encode(newPassword));
        nicknameChangeHistoryRepository.save(nicknameChangeHistory);
        userRepository.save(user);
        return LanguageMessage.getText("successfully");
    }

    public String updateUserLoginAdmin(final String newLogin, final String login) {
        if (!userRepository.existsByLogin(login)) return LanguageMessage.getText("user.not.found");
            else {
                final User user = userRepository.findByLogin(login);
                if (user.getLogin().equals(newLogin)) return LanguageMessage.getText("error.create");
                final User findByLogin = userRepository.findByLogin(newLogin);
                if (findByLogin != null && !findByLogin.getId().equals(user.getId()))
                    return LanguageMessage.getText("user.exists");
                else {
                    user.setLogin(newLogin);
                    userRepository.save(user);
                    return LanguageMessage.getText("successfully");
                }
            }
    }

    public String updateUserNicknameAdmin(final String newNickname, final String login) {
        if (!userRepository.existsByLogin(login)) return LanguageMessage.getText("user.not.found");
            else {
                final User user = userRepository.findByLogin(login);
                if (user.getNickname().equals(newNickname)) return LanguageMessage.getText("error.create");
                final NicknameChangeHistory nicknameChangeHistory = new NicknameChangeHistory();
                nicknameChangeHistory.setNewNickname(newNickname);
                nicknameChangeHistory.setOldNickname(user.getNickname());
                nicknameChangeHistory.setUserId(user.getId());
                nicknameChangeHistory.setDate(new Date());
                user.setNickname(newNickname);
                nicknameChangeHistoryRepository.save(nicknameChangeHistory);
                userRepository.save(user);
                return LanguageMessage.getText("successfully");
            }
    }

    public List<NicknameChangeHistoryDto> getHistory() {
        return  nicknameChangeHistoryRepository.findAll().stream()
                .map(nicknameChangeHistoryConverter::convertToDto).collect(Collectors.toList());
    }

    public List<UserDto> getUserList(final String filter) {
        if (filter==null)
            return userRepository.findAll().stream().map(userConverter::convertToDto).collect(Collectors.toList());
            else {
                final List<UserDto> list = userRepository.findAll().stream().map(userConverter::convertToDto).collect(Collectors.toList());
                final List<UserDto> sortedList = new ArrayList<>();
                for (UserDto user:list) {
                    if (user.getLogin().toLowerCase().contains(filter.toLowerCase())) sortedList.add(user);
                }
                return sortedList;
            }
    }

    public Long getUserCount(final String filter){
        if (filter==null) return userRepository.count();
            else return (long) this.getUserList(filter).size();
    }

    public String deleteUser(final String login) {
        if (login.equals(UserDto.ROOT)) return LanguageMessage.getText("access.error");
            else if (userRepository.existsByLogin(login)) {
                userRepository.delete(userRepository.findByLogin(login));
                return LanguageMessage.getText("successfully");
            } else return LanguageMessage.getText("user.not.found");
    }

    public User getUser(final String login) {
        return userRepository.findByLogin(login);
    }

    public String changeRoom(final String name, final String login) {
        if (roomRepository.existsByName(name)) {
            final User user = userRepository.findByLogin(login);
            user.setRoom(roomRepository.findByName(name));
            userRepository.save(user);
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("room.not.found");
    }

    public String changeRoleUser(final String login) {
        if (login.equals(UserDto.ROOT)) return LanguageMessage.getText("access.error");
            else if (userRepository.existsByLogin(login)) {
                final User user = userRepository.findByLogin(login);
                if (user.getRole().equals(RoleEnum.ROLE_USER.toString())) user.setRole(RoleEnum.ROLE_ADMIN.toString());
                else user.setRole(RoleEnum.ROLE_USER.toString());
                userRepository.save(user);
                return LanguageMessage.getText("successfully");
            } else return LanguageMessage.getText("user.not.found");
    }

    public List<String> getNameUserList(){
        final List<User> userList = userRepository.findAll();
        final List<String> nameUserList = new ArrayList<>();
        for (User user: userList) {
            nameUserList.add(user.getLogin());
        }
        return nameUserList;
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }

    @Autowired
    private PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
