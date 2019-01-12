package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.Room;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class RoomConverter{

    public RoomDto convertToDto(final Room room) {
        final RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(room, roomDto);
        return roomDto;
    }

    public Room convertToDbo(final RoomDto roomDto) {
        final Room room = new Room();
        BeanUtils.copyProperties(roomDto, room);
        return room;
    }
}
