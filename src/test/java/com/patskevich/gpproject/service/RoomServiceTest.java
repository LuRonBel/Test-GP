package com.patskevich.gpproject.service;

import com.patskevich.gpproject.MockDataMessage;
import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.RoomConverter;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Spy
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
        assertEquals(LanguageMessage.getText("successfully"), answer);
        verify(roomRepository, times(1)).save(null);
    }

    @Test
    public void deleteRoom() {
        final Room room = MockDataRoom.getRoom();
        final Room defaultRoom = MockDataRoom.getRoom();
        final User user = MockDataUser.getUser();
        final Message message = MockDataMessage.getMessage();
        final List<User> listUser = new ArrayList<>();
        final List<Message> listMessage = new ArrayList<>();
        defaultRoom.setName("default room");
        listMessage.add(message);
        listUser.add(user);
        user.setRoom(room);
        message.setAuthor(user);
        message.setRoom(room);
        doReturn(true).when(roomRepository).existsByName(room.getName());
        doReturn(room).when(roomRepository).findByName(room.getName());
        doReturn(listUser).when(userRepository).findAllByRoom(room);
        doReturn(defaultRoom).when(roomRepository).findByName(RoomDto.DEFAULT_ROOM);
        doReturn(listMessage).when(messageRepository).findAllByRoom(room);

        String answer = roomService.deleteRoom(room.getName());
        assertEquals(LanguageMessage.getText("successfully"), answer);
        room.setName(RoomDto.DEFAULT_ROOM);

        answer = roomService.deleteRoom(room.getName());
        assertEquals(LanguageMessage.getText("access.error"), answer);
        verify(roomRepository, times(1)).delete(room);
        verify(userRepository, times(1)).save(user);
        verify(messageRepository, times(1)).delete(message);
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
        assertEquals(LanguageMessage.getText("successfully"), answer);
        verify(roomRepository, times(1)).save(room);
        assertEquals(roomDto.getDescription(), room.getDescription());
    }

    @Test
    public void getRoomList(){
        final Room room1 = MockDataRoom.getRoom();
        final Room room2 = new Room();
        final RoomDto roomDto1 = MockDataRoom.getRoomDto();
        final RoomDto roomDto2 = new RoomDto();
        roomDto1.setName("first");
        roomDto2.setName("second");
        final List<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        doReturn(list).when(roomRepository).findAll();
        doReturn(roomDto1).when(roomConverter).convertToDto(room1);
        doReturn(roomDto2).when(roomConverter).convertToDto(room2);
        List<RoomDto> answer = roomService.getRoomList(null);

        assertEquals(2, answer.size());
        assertEquals(roomDto1, answer.get(0));
        assertEquals(roomDto2, answer.get(1));
        answer = roomService.getRoomList("e");

        assertEquals(1, answer.size());
        assertEquals(roomDto2, answer.get(0));
        verify(roomRepository, times(2)).findAll();
        verify(roomConverter, times(4)).convertToDto(any(Room.class));
    }

    @Test
    public void getNameRoomList(){
        final Room room1 = MockDataRoom.getRoom();
        final Room room2 = new Room();
        final List<Room> list = new ArrayList<>();
        room1.setName("first");
        room2.setName("second");
        list.add(room1);
        list.add(room2);
        doReturn(list).when(roomRepository).findAll();
        final List<String> answer = roomService.getNameRoomList();

        assertEquals(2, answer.size());
        assertEquals(room1.getName(), answer.get(0));
        assertEquals(room2.getName(), answer.get(1));
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void getRoom(){
        final Room room = MockDataRoom.getRoom();
        doReturn(room).when(roomRepository).findByName(room.getName());
        final RoomDto answer = roomService.getRoom(room.getName());

        verify(roomConverter, times(1)).convertToDto(room);
    }

    @Test
    public void getRoomEntity(){
        final Room room = MockDataRoom.getRoom();
        doReturn(room).when(roomRepository).findByName(room.getName());
        final Room answer = roomService.getRoomEntity(room.getName());

        assertEquals(room, answer);
        verify(roomRepository, times(1)).findByName(room.getName());
    }

    @Test
    public void getRoomCount(){
        final Room room1 = MockDataRoom.getRoom();
        final Room room2 = new Room();
        final RoomDto roomDto1 = MockDataRoom.getRoomDto();
        final RoomDto roomDto2 = new RoomDto();
        roomDto1.setName("first");
        roomDto2.setName("second");
        final List<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        doReturn((long)list.size()).when(roomRepository).count();
        doReturn(list).when(roomRepository).findAll();
        doReturn(roomDto1).when(roomConverter).convertToDto(room1);
        doReturn(roomDto2).when(roomConverter).convertToDto(room2);
        Long answer = roomService.getRoomCount(null);

        assertEquals((long)list.size(), (long)answer);
        verify(roomRepository, times(1)).count();

        answer = roomService.getRoomCount("e");
        assertEquals((long)1, (long)answer);
    }


}
