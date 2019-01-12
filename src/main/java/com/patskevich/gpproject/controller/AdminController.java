package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.dto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(AdminController.CURRENT_PAGE_URL)
public class AdminController {

    public static final String CURRENT_PAGE_URL = "/admin";
    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/create_room")
    public String createRoom(@RequestBody final RoomDto room) {
        return roomService.createRoom(room);
    }

    @DeleteMapping("/delete_room")
    public String deleteRoom(@RequestBody final String roomName) {
        return roomService.deleteRoom(roomName);
    }

    @PostMapping("/update_room")
    public String updateRoom(@RequestBody final RoomDto roomDto) {
        return roomService.updateRoom(roomDto);
    }

    @PostMapping("/create_user")
    public String createUser(@RequestParam(name = "login") final String login,
                             @RequestParam(name = "password") final String password) {
        return userService.createUser(login, password);
    }

    @PostMapping("/change_user/{login}/change_role")
    public String changeRoleUser(@PathVariable("login") final String login) {
        return userService.changeRoleUser(login);
    }

    @PostMapping("/change_user/{login}/change_login")
    public String changeUserName(@RequestBody final String newLogin, @PathVariable("login") final String login) {
        return userService.updateUserLoginAdmin(newLogin, login);
    }

    @PostMapping("/change_user/{login}/change_nickname")
    public String changeUserNickname(@RequestBody final String newNickname, @PathVariable("login") final String login) {
        return userService.updateUserNicknameAdmin(newNickname, login);
    }

    @GetMapping("/change_history")
    public List<NicknameChangeHistoryDto> getHistory() {
        return userService.getHistory();
    }

    @DeleteMapping("/delete_user")
    public String deleteUser(@RequestBody final String login) {
        return userService.deleteUser(login);
    }

    @GetMapping("/list_user")
    public List<UserDto> getAllUser() {
        return userService.getUserList(null);
    }

    @DeleteMapping("/delete_message/{id}")
    public String deleteMessage(@PathVariable("id") final Long id) {
        return messageService.deleteMessage(id);
    }
}
