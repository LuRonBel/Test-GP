package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    public void convertToDto() {
        final User user = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();
        user.setRoom(room);
        final UserDto answer = userConverter.convertToDto(user);

        assertEquals(user.getLogin(), answer.getLogin());
        assertEquals(user.getNickname(), answer.getNickname());
        assertEquals(user.getRoom().getName(), answer.getRoom());
        assertEquals(user.getRole(), answer.getRole());
    }

    @Test
    public void convertToDbo() {
        final CreateUserDtoUi user = MockDataUser.getCreateUserDtoUi();
        final Room room = MockDataRoom.getRoom();
        user.setRoom(room);
        final User answer = userConverter.convertToDbo(user);

        assertEquals(user.getLogin(), answer.getLogin());
        assertEquals(user.getNickname(), answer.getNickname());
        assertEquals(user.getPassword(), answer.getPassword());
        assertEquals(room, answer.getRoom());
        assertEquals( user.getRole(), answer.getRole());
    }
}
