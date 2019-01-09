package com.patskevich.gpproject.service;


import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.dto.UserDto.CreateUserDto;
import com.patskevich.gpproject.dto.UserDto.UpdateUserDto;
import com.patskevich.gpproject.dto.UserDto.UserNameDto;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.List;

@Service
@AllArgsConstructor
public class UiService {

    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;

    public void updateGrid(final String login, final Grid grid){
        grid.setItems(messageService.getRoomMessage(login));
    }

    public void updateRoomSelected(final ComboBox comboBox, final String login){
        comboBox.setItems(roomService.getNameRoomList());
        comboBox.setSelectedItem(userService.getUser(login).getRoom().getName());
    }

    public void updateRoomSelectedAdminPanel(final ComboBox comboBox, final Label label){

        final List<String> list = roomService.getNameRoomList();
        comboBox.setItems(list);
        label.setValue("Room count: "+list.size());
    }

    public void updateUserSelected(final ComboBox comboBox, final Label label){

        final List<String> list = userService.getNameUserList();
        label.setValue("User count: "+list.size());
        comboBox.setItems(list);
    }

    public void updateRoomInfo(final String selectRoom, final TextField roomName, final TextField roomDescription){

            RoomDto room = roomService.getRoom(selectRoom);
            roomName.setValue(room.getName());
            roomDescription.setValue(room.getDescription());
    }

    public void updateUserInfoAdminPanel(final String selectUser, final Label userNickname, final Label userRole){

        User user = userService.getUser(selectUser);
        userNickname.setValue("Nickname: "+user.getNickname());
        userRole.setValue("Role: "+user.getRole());
    }

    public void updateUserInfo(final Label label, final String login){
        User user = userService.getUser(login);
        label.setValue("Login: "+user.getLogin()+" | Role: "+user.getRole());
    }

    public void updateLog(final Label log, final String text){
        log.setValue("LOG: "+text);
    }

    public void updateRoomDecription(final Label label, final String login){
        label.setValue("Description: "+userService.getUser(login).getRoom().getDescription());
    }

    public String changeRoom(final String room, final String login){
        return userService.changeRoom(room, login);
    }

    public String getUserRole(final String login){
        return userService.getUser(login).getRole();
    }

    public String addMessage(final String text, final String login){
        return messageService.addMessage(text, login);
    }

    public String createRoom(final String name, final String description){
            RoomDto room = new RoomDto();
            room.setName(name);
            room.setDescription(description);
            return roomService.createRoom(room);
    }

    public String deleteRoom(final String name){
        NameRoomDto room = new NameRoomDto();
        room.setName(name);
        return roomService.deleteRoom(room);
    }

    public String updateRoom(final String name, final String description){
        final RoomDto room = new RoomDto();
        room.setName(name);
        room.setDescription(description);
        return roomService.updateRoom(room);
    }

    public String createUser(final String login, final String password){
        CreateUserDto user = new CreateUserDto();
        user.setLogin(login);
        user.setPassword(password);
        return userService.createUser(user);
    }

    public String deleteUser(final String login){
        UserNameDto user = new UserNameDto();
        user.setName(login);
        return userService.deleteUser(user);
    }

    public String changeRole(final String login){
        UserNameDto user = new UserNameDto();
        user.setName(login);
        return userService.changeRoleUser(user);
    }

    public void updateLogGrid(final Grid grid){
        grid.setItems(userService.getHistory());
    }

    public String changeNickname(final String login, final String newNickname){
        return userService.updateUserNicknameAdmin(newNickname, login);
    }

    public String getNickname(final String login){
        return userService.getUser(login).getNickname();
    }
}
