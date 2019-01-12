package com.patskevich.gpproject.service;

import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.User;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UiService {

    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;

    public void updateGrid(final String login, final Grid grid){
        grid.setItems(messageService.getMessage(login));
    }

    public void updateRoomSelected(final ComboBox comboBox, final String login){
        comboBox.setItems(roomService.getNameRoomList());
        comboBox.setSelectedItem(userService.getUser(login).getRoom().getName());
    }

    public void updateRoomSelectedAdminPanel(final ComboBox comboBox, final Label label){
        final List<String> list = roomService.getNameRoomList();
        comboBox.setItems(roomService.getNameRoomList());
        label.setValue("Room count: "+list.size());
    }

    public void updateUserSelected(final ComboBox comboBox, final Label label){
        final List<String> list = userService.getNameUserList();
        label.setValue("User count: "+list.size());
        comboBox.setItems(list);
    }

    public void updateRoomInfo(final String selectRoom, final TextField roomName, final TextField roomDescription){
        final RoomDto room = roomService.getRoom(selectRoom);
        roomName.setValue(room.getName());
        roomDescription.setValue(room.getDescription());
    }

    public void updateUserInfoAdminPanel(final String selectUser, final Label userNickname, final Label userRole){
        final User user = userService.getUser(selectUser);
        userNickname.setValue("Nickname: "+user.getNickname());
        userRole.setValue("Role: "+user.getRole());
    }

    public void updateUserInfo(final Label label, final String login){
        final User user = userService.getUser(login);
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
        final RoomDto room = new RoomDto();
        room.setName(name);
        room.setDescription(description);
        return roomService.createRoom(room);
    }

    public String deleteRoom(final String name){
        return roomService.deleteRoom(name);
    }

    public String updateRoom(final String name, final String description){
        final RoomDto room = new RoomDto();
        room.setName(name);
        room.setDescription(description);
        return roomService.updateRoom(room);
    }

    public String createUser(final String login, final String password){
        return userService.createUser(login, password);
    }

    public String deleteUser(final String login){
        return userService.deleteUser(login);
    }

    public String changeRole(final String login){
        return userService.changeRoleUser(login);
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
