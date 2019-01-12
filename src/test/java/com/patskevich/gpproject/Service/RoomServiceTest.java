package com.patskevich.gpproject.Service;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.converter.RoomConverter;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import com.patskevich.gpproject.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomConverter roomConverter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void createRoom() {

        final RoomDto roomDto = MockDataRoom.getRoomDto();

        doReturn(false).when(roomRepository).existsByName(roomDto.getName());

        final String answer = roomService.createRoom(roomDto);

        assertEquals("Команта "+roomDto.getName()+" была создана!", answer);
        verify(roomRepository, times(1)).save(null);
    }
/*
    @Test
    public void deleteRoom() {

        final NameRoomDto nameRoomDto = MockDataRoom.getNameRoomDto();
        final User user = MockDataUser.getUser();
        final Message message1 = MockDataMessage.getMessage();
        final Message message2 = MockDataMessage.getMessage();
        final Room room = MockDataRoom.getRoom();
        final Room defaultRoom = MockDataRoom.getRoom();
        final List<Message> listMessage = new ArrayList<>();
        final List<User> listUser = new ArrayList<>();
        message1.setRoom(room);
        message2.setRoom(room);
        user.setRoom(room);
        listMessage.add(message1);
        listMessage.add(message2);
        listUser.add(user);
        defaultRoom.setName("default room");

        doReturn(true).when(roomRepository).existsByName(nameRoomDto.getName());
        doReturn(room).when(roomRepository).findByName(nameRoomDto.getName());
        doReturn(listUser).when(userRepository).findAllByRoom(room);
        doReturn(defaultRoom).when(roomRepository).findByName("Default room");
        doReturn(listMessage).when(messageRepository).findAllByRoom(room);

        final String answer = roomService.deleteRoom(nameRoomDto);

        verify(roomRepository, times(1)).delete(room);
        verify(userRepository, times(1)).save(user);
        verify(messageRepository, times(2)).delete(any(Message.class));

        assertEquals(defaultRoom, user.getRoom());
    }

    @Test
    public void updateRoom(){

        final RoomDto roomDto = MockDataRoom.getRoomDto();
        final Room room = MockDataRoom.getRoom();
        roomDto.setName("new name");
        roomDto.setDescription("new description");

        doReturn(true).when(roomRepository).existsByName(roomDto.getName());
        doReturn(room).when(roomRepository).findByName(roomDto.getName());

        final String answer = roomService.updateRoom(roomDto);

        verify(roomRepository, times(1)).save(room);
        assertEquals(roomDto.getDescription(), room.getDescription());


    }

    @Test
    public void getRoomList(){
        final List<RoomDto> userList = roomService.getRoomList();
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void getNameRoomList(){

        final List<String> list = roomService.getNameRoomList();

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void getRoom(){

        final Room room = MockDataRoom.getRoom();

        doReturn(room).when(roomRepository).findByName(room.getName());

        final RoomDto roomDto = roomService.getRoom(room.getName());

        verify(roomConverter, times(1)).convertToDto(room);

    }*/
}
