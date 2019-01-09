package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataMessage;
import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MessageConverterTest {

    @InjectMocks
    private MessageConverter messageConverter;
    @Mock
    private UserRepository userRepository;

    @Spy
    private SimpleDateFormat simpleDateFormat;


    @Test
    public void convertToDto() {

        final User author = MockDataUser.getUser();
        final Message message = MockDataMessage.getMessage();
        message.setAuthor(author);

        final MessageOutputDto messageOutputDto = messageConverter.convertToDto(message);

        assertEquals(messageOutputDto.getMessage(), message.getMessage());
        assertEquals(messageOutputDto.getAuthor(), message.getAuthor().getNickname());
        assertEquals(messageOutputDto.getDate(), message.getDate());
    }

    @Test
    public void convertToDbo() {

        final Room room = MockDataRoom.getRoom();
        final User author = MockDataUser.getUser();
        final MessageInputDto messageInputDto = MockDataMessage.getMessageInputDto();
        author.setRoom(room);

        doReturn(author).when(userRepository).findByLogin(author.getLogin());

        final Message message = messageConverter.convertToDbo(messageInputDto,author.getLogin());

        assertEquals(author, message.getAuthor());
        assertEquals(room, message.getRoom());
        assertEquals(messageInputDto.getMessage(), message.getMessage());
        assertEquals(new SimpleDateFormat("dd.MM.yyyy").format(new Date()), message.getDate());
    }
}