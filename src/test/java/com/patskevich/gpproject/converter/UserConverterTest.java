package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Mock
    private RoomRepository roomRepository;

    @Test
    public void convertToDto() {

        final User user = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();
        user.setRoom(room);

        final UserDto userDto = userConverter.convertToDto(user);

        assertEquals(user.getLogin(), userDto.getLogin());
        assertEquals(user.getNickname(), userDto.getNickname());
        assertEquals(user.getRoom(), userDto.getRoom());
        assertEquals(user.getRole(), userDto.getRole());
    }
 /*
    @Test
    public void convertToDbo() {

        final CreateUserDto createUserDto = MockDataUser.getCreateUserDto();
        final Room room = MockDataRoom.getRoom();

        doReturn(room).when(roomRepository).findByName("Default room");

        final User user = userConverter.convertToDbo(createUserDto);

        assertEquals(user.getLogin(), createUserDto.getLogin());
        assertEquals(user.getPassword(), createUserDto.getPassword());
        assertEquals("New user", user.getNickname());
        assertEquals(user.getRoom(), room);
        assertEquals( "ROLE_USER", user.getRole());
    }*/
}
