package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.Room;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomConverterTest {

    private final RoomConverter roomConverter = new RoomConverter();

    @Test
    public void convertToDto() {
        final Room room = MockDataRoom.getRoom();
        final RoomDto roomDto = roomConverter.convertToDto(room);

        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getDescription(), roomDto.getDescription());
    }

    @Test
    public void convertToDbo() {
        final RoomDto roomDto = MockDataRoom.getRoomDto();
        final Room room = roomConverter.convertToDbo(roomDto);

        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getDescription(), roomDto.getDescription());
    }
}
