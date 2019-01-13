package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.Room;

public class MockDataRoom {

    public static Room getRoom() {
        final Room room = new Room();
        room.setId(1L);
        room.setName("name");
        room.setDescription("description");
        return room;
    }

    public static RoomDto getRoomDto() {
        final RoomDto roomDto = new RoomDto();
        roomDto.setName("name");
        roomDto.setDescription("description");
        return roomDto;
    }

}