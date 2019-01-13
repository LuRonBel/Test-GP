package com.patskevich.gpproject.service;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.RoomConverter;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
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
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("room.exists");
    }

    public String deleteRoom(final String roomName) {
        if (roomName.equals(RoomDto.DEFAULT_ROOM)) return LanguageMessage.getText("access.error");
            else if (roomRepository.existsByName(roomName)) {
                setDefaultRoom(roomName);
                deleteMessageFromRoom(roomName);
                roomRepository.delete(roomRepository.findByName(roomName));
                return LanguageMessage.getText("successfully");
            } else return LanguageMessage.getText("room.not.found");
    }

    public String updateRoom(final RoomDto roomDto) {
        if (roomRepository.existsByName(roomDto.getName())) {
            final Room room  = roomRepository.findByName(roomDto.getName());
            room.setDescription(roomDto.getDescription());
            roomRepository.save(room);
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("room.not.found");
    }

    public List<RoomDto> getRoomList(final String filter) {
        if (filter==null) {
            return roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
        }
        else {
            final List<RoomDto> list = roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
            final List<RoomDto> sortedList = new ArrayList<>();
            for (RoomDto room:list) {
                if (room.getName().toLowerCase().contains(filter.toLowerCase())) sortedList.add(room);
            }
            return sortedList;
        }
    }

    public List<String> getNameRoomList(){
        final List<Room> roomList = roomRepository.findAll();
        final List<String> nameRoomList = new ArrayList<>();
        for (Room room: roomList) {
            nameRoomList.add(room.getName());
        }
        return nameRoomList;
    }

    public RoomDto getRoom(final String name) {
        return roomConverter.convertToDto(roomRepository.findByName(name));
    }

    public Room getRoomEntity(final String name) {
        return roomRepository.findByName(name);
    }

    public Long getRoomCount(final String filter){
        if (filter==null) return roomRepository.count();
            else return (long) this.getRoomList(filter).size();
    }

    private void setDefaultRoom(final String roomName){
        final List<User> list = userRepository.findAllByRoom(roomRepository.findByName(roomName));
        for (User user : list){
            user.setRoom(roomRepository.findByName(RoomDto.DEFAULT_ROOM));
            userRepository.save(user);
        }
    }

    private void deleteMessageFromRoom(final String roomName){
        final List<Message> list = messageRepository.findAllByRoom(roomRepository.findByName(roomName));
        for (Message message : list){
            messageRepository.delete(message);
        }
    }
}
