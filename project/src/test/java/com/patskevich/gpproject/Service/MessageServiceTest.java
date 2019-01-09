package com.patskevich.gpproject.Service;

import com.patskevich.gpproject.MockDataMessage;
import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.dto.MessageDto.MessageCorrectDto;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import com.patskevich.gpproject.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private RoomRepository roomRepository;

    @Test
    public void addMessage(){

        final MessageInputDto messageInputDto = MockDataMessage.getMessageInputDto();
        final User user = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();
        user.setRoom(room);

        doReturn(user).when(userRepository).findByLogin(user.getLogin());

        final String answer = messageService.addMessage(messageInputDto,user.getLogin());

        verify(messageConverter, times(1)).convertToDbo(messageInputDto,user.getLogin());
        verify(messageRepository, times(1)).save(null);
        assertEquals("Сообщение было создано в комнате "+room.getName()+".", answer);
    }

    @Test
    public void getRoomMesage(){

        final User user = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();
        user.setRoom(room);

        doReturn(user).when(userRepository).findByLogin(user.getLogin());

        messageService.getRoomMessage(user.getLogin());

        verify(messageRepository, times(1)).findAllByRoomOrderByIdDesc(room);
    }

    @Test
    public void deleteMessage(){

        final Message message = MockDataMessage.getMessage();

        doReturn(true).when(messageRepository).existsById(message.getId());

        final String answer = messageService.deleteMessage(message.getId());

        verify(messageRepository, times(1)).deleteById(message.getId());
        assertEquals("Сообщение с ID №" + message.getId() + " было удалено!", answer);
    }
}
