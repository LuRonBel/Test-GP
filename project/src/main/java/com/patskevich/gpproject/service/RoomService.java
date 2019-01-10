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
            return "Комната "+roomDto.getName()+" уже существует!";
    }

    public String deleteRoom(final NameRoomDto deleteRoomDto) {
        if (deleteRoomDto.getName().equals("Default room")) return "Эту комнату удалить нельзя!";
        else if (roomRepository.existsByName(deleteRoomDto.getName())) {
                    setDefaultRoom(deleteRoomDto.getName());
                    deleteMessageFromRoom(deleteRoomDto.getName());
                    roomRepository.delete(roomRepository.findByName(deleteRoomDto.getName()));
                    return "Команта "+deleteRoomDto.getName()+" была удалена!";
            } else
                    return "Комнаты "+deleteRoomDto.getName()+" не существует!";
    }

    public String deleteRoom(final RoomDto roomDto) {
        if (roomDto.getName().equals("Default room")) return "Эту комнату удалить нельзя!";
        else if (roomRepository.existsByName(roomDto.getName())) {
            setDefaultRoom(roomDto.getName());
            deleteMessageFromRoom(roomDto.getName());
            roomRepository.delete(roomRepository.findByName(roomDto.getName()));
            return "Команта "+roomDto.getName()+" была удалена!";
        } else
            return "Комнаты "+roomDto.getName()+" не существует!";
    }

    public String updateRoom(final RoomDto roomDto) {
        if (roomRepository.existsByName(roomDto.getName())) {
            final Room room  = roomRepository.findByName(roomDto.getName());
            room.setDescription(roomDto.getDescription());
            roomRepository.save(room);
            return "Комната "+roomDto.getName()+" была обновлена!";
        } else
            return "Комнаты "+roomDto.getName()+" не существует!";
    }

    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
    }

    public List<RoomDto> getRoomList(final String filter) {
        if (filter==null) {
            return roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
        }
        else {
            final List<RoomDto> list = roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
            final List<RoomDto> sortedList = new ArrayList<>();
            for (RoomDto room:list) {
                if (room.getName().toLowerCase().contains(filter.toLowerCase())){
                    sortedList.add(room);
                }
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

    public Long getRoomCount(final String filter){
        if (filter==null) {
            return roomRepository.count();
        }
        else {
            final List<RoomDto> list = roomRepository.findAll().stream().map(roomConverter::convertToDto).collect(Collectors.toList());
            Long count = 0l;
            for (RoomDto room:list) {
                if (room.getName().toLowerCase().contains(filter.toLowerCase())){
                    count++;
                }
            }
            return count;
        }
    }

    private void setDefaultRoom(final String roomName){
        final List<User> list = userRepository.findAllByRoom(roomRepository.findByName(roomName));
        for (User user : list){
            user.setRoom(roomRepository.findByName("Default room"));
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
