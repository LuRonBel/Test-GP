package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(UserController.CURRENT_PAGE_URL)
public class UserController {

    public static final String CURRENT_PAGE_URL = "/user";
    private final UserService userService;
    private final RoomService roomService;
    private final MessageService messageService;

    @PostMapping("/change_nick_pass")
    public String changeUserNickname(@RequestParam(name = "newNickname") final String newNickname,
                                     @RequestParam(name = "newPassword") final String newPassword) {
        return userService.changeNicknameAndPass(newNickname, newPassword, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @PostMapping("/change_room")
    public String changeRoom(@RequestBody final String roomName) {
        return userService.changeRoom(roomName,SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @GetMapping("/list_room")
    public List<RoomDto> getAllRoom() {
        return roomService.getRoomList(null);
    }

    @PostMapping("/add_message")
    public String addMessage(@RequestBody final String message) {
        return messageService.addMessage(message, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @GetMapping("/list_message")
    public List<MessageDto> getMessage() {
        return messageService.getMessage(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @PutMapping("/edit_message/{id}")
    public String editMessage(@RequestBody final String newMessage, @PathVariable("id") final Long id) {
        return messageService.editMessage(id, newMessage, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @GetMapping("/search")
    public List<MessageDto> findMessages(@RequestParam(name = "fromDate") final String fromDate,
                                         @RequestParam(name = "toDate") final String toDate) {
        return messageService.findMessages(fromDate, toDate, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }
}