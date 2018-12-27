package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.RoomConverter;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomConverter roomConverter;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    public String createRoom(final RoomDto roomDto) {
        if (!roomRepository.existsByName(roomDto.getName())) {
            roomRepository.save(roomConverter.convertToDbo(roomDto));
            return "Команта "+roomDto.getName()+" была создана!";
        } else
            return "Комнаты "+roomDto.getName()+" уже существует!";
    }

    public String deleteRoom(final NameRoomDto deleteRoomDto) {
        if (roomRepository.existsByName(deleteRoomDto.getName())) {
                setDefaultRoom(deleteRoomDto);
                deleteMessageFromRoom(deleteRoomDto);
                roomRepository.delete(roomRepository.findByName(deleteRoomDto.getName()));
                return "Команта "+deleteRoomDto.getName()+" была удалена!";
        } else
                return "Комнаты "+deleteRoomDto.getName()+" не существует!";
    }

    public String updateRoom(final RoomDto roomDto) {
        if (!roomRepository.existsByName(roomDto.getName())) {
            Room room  = roomRepository.findByName(roomDto.getName());
            room.setName(roomDto.getName());
            room.setDescription(roomDto.getDescription());
            roomRepository.save(room);
            return "Команта "+roomDto.getName()+" была обновлена!";
        } else
            return "Комнаты "+roomDto.getName()+" не существует!";
    }

    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
    }

    public List<String> getNameRoomList(){
        List<Room> roomList = roomRepository.findAll();
        List<String> nameRoomList = new ArrayList<>();
        for (Room room: roomList) {
            nameRoomList.add(room.getName());
        }
        return nameRoomList;
    }

    public RoomDto getRoomByName(String name) {
        return roomConverter.convertToDto(roomRepository.findByName(name));
    }

    private void setDefaultRoom(final NameRoomDto deleteRoomDto){
        List<User> list = userRepository.findAllByRoom(roomRepository.findByName(deleteRoomDto.getName()));
        for (User user : list){
            user.setRoom(roomRepository.findByName("Default room"));
            userRepository.save(user);
        }
    }

    private void deleteMessageFromRoom(final NameRoomDto deleteRoomDto){
        List<Message> list = messageRepository.findAllByRoom(roomRepository.findByName(deleteRoomDto.getName()));
        for (Message message : list){
            messageRepository.delete(message);
        }
    }
}
