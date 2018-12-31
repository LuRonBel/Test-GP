package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.dto.NicknameChangeHistoryDto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.dto.UserDto.CreateUserDto;
import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.dto.UserDto.UserNameDto;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final RoomService roomService;
    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/create_room")
    public String createRoom(@RequestBody final RoomDto room) {
        return roomService.createRoom(room);
    }

    @DeleteMapping("/delete_room")
    public String deleteRoom(@RequestBody final NameRoomDto deleteRoomDto) {
        return roomService.deleteRoom(deleteRoomDto);
    }

    @PostMapping("/update_room")
    public String updateRoom(@RequestBody final RoomDto roomDto) {
        return roomService.updateRoom(roomDto);
    }

    @PostMapping("/create_user")
    public String createUser(@RequestBody final CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @PostMapping("/change_role_user")
    public String changeRoleUser(@RequestBody final UserNameDto userNameDto) {
        return userService.changeRoleUser(userNameDto);
    }

    @PostMapping("/change_user/{id}/change_name")
    public String changeUserName(@RequestBody final String newName, @PathVariable("id") final Long id) {
        return userService.updateUserNameAdmin(newName, id);
    }

    @PostMapping("/change_user/{id}/change_nickname")
    public String changeUserNickname(@RequestBody final String newNickname, @PathVariable("id") final Long id) {
        return userService.updateUserNicknameAdmin(newNickname, id);
    }

    @GetMapping("/change_history")
    public List<NicknameChangeHistoryDto> getHistory() {
        return userService.getHistory();
    }

    @DeleteMapping("/delete_user")
    public String deleteUser(@RequestBody final UserNameDto nameUserDto) {
        return userService.deleteUser(nameUserDto);
    }

    @GetMapping("/list_user")
    public List<UserDto> getAllUser() {
        return userService.getUserList();
    }

    @DeleteMapping("/delete_message/{id}")
    public String deleteMessage(@PathVariable("id") final Long id) {
        return messageService.deleteMessage(id);
    }
    /*
     @PostMapping("/room_by_name")
     public RoomDto getRoomByName(@RequestBody final NameRoomDto nameRoomDto) {
     return roomService.getRoomByName(nameRoomDto);
     }
    */
}
