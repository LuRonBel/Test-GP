package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.dto.UserDto.*;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoomService roomService;
    private final MessageService messageService;

    @PostMapping("/update_user")
    public String updateUser(@RequestBody final UpdateUserDto updateUserDto) {
        return userService.updateUser(updateUserDto);
    }

    @PostMapping("/change_room")
    public String changeRoom(@RequestBody final UserChangeRoom userChangeRoom) {
        return userService.changeRoom(userChangeRoom);
    }

    @GetMapping("/list_room")
    public List<RoomDto> getAllRoom() {
        return roomService.getRoomList();
    }

    @PostMapping("/add_message")
    public String addMessage(@RequestBody final MessageInputDto messageInputDto) {
        return messageService.addMessage(messageInputDto);
    }

    @GetMapping("/list_message")
    public List<MessageOutputDto> getRoomMessage (@RequestBody final MessageRoomDto messageRoomDto) {
        return messageService.getRoomMesage(messageRoomDto);
    }

    @PutMapping("/edit_message/{id}")
    public String editMessage(
            @PathVariable("id") final Long id,
            @RequestBody final MessageInputDto messageInputDto) {
        return messageService.editMessage(id, messageInputDto);
    }

    @GetMapping("/search")
    public List<MessageOutputDto> findMessages(
            @RequestBody final MessageRoomDto roomDto,
            @RequestParam(name = "fromDate") final String fromDate,
            @RequestParam(name = "toDate") final String toDate) {
        return messageService.findMessages(roomDto, fromDate, toDate);
    }
}