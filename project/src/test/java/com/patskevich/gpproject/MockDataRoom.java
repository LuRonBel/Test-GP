package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.entity.Room;

public class MockDataRoom {

    public static Room getRoom() {
        final Room room = new Room();
        room.setId(new Long(1));
        room.setName("name");
        room.setDescription("description");
        return room;
    }

    public static NameRoomDto getNameRoomDto() {
        final NameRoomDto nameRoomDto = new NameRoomDto();
        nameRoomDto.setName("name");
        return nameRoomDto;
    }

    public static RoomDto getRoomDto() {
        final RoomDto roomDto = new RoomDto();
        roomDto.setName("name");
        roomDto.setDescription("description");
        return roomDto;
    }

}