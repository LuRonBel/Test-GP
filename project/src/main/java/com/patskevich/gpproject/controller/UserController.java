package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.dto.MessageDto.MessageCorrectDto;
import com.patskevich.gpproject.dto.MessageDto.MessageDateDto;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.dto.UserDto.*;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoomService roomService;
    private final MessageService messageService;

    @PostMapping("/change_nick_pass")
    public String changeUserNickname(@RequestBody final UpdateUserDto updateUserDto) {
        return userService.changeNicknameAndPass(updateUserDto, SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
    }

    @PostMapping("/correct_message")
    public String correctMessage(@RequestBody final MessageCorrectDto messageCorrectDto) {
        return messageService.correctMessage(messageCorrectDto, SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
    }

    @PostMapping("/change_room")
    public String changeRoom(@RequestBody final NameRoomDto nameRoomDto) {
        return userService.changeRoom(nameRoomDto,SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
    }

    @GetMapping("/list_room")
    public List<RoomDto> getAllRoom() {
        return roomService.getRoomList();
    }

    @PostMapping("/add_message")
    public String addMessage(@RequestBody final MessageInputDto messageInputDto) {
        return messageService.addMessage(messageInputDto, SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
    }

    @GetMapping("/list_message")
    public List<MessageOutputDto> getRoomMessage () {
        return messageService.getRoomMessage(SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
    }

    @GetMapping("/list_message_date")
    public List<MessageOutputDto> getMessageByDate (@RequestBody final MessageDateDto messageDateDto) {
        return messageService.getMessageByDate(SecurityContextHolder
                .getContext()
                .getAuthentication().getName(), messageDateDto.getDateFrom(), messageDateDto.getDateTo());
    }

}