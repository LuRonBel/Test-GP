package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataMessage;
import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MessageConverterTest {

    @InjectMocks
    private MessageConverter messageConverter;
    @Mock
    private UserRepository userRepository;

    @Test
    public void convertToDto() {
        final User author = MockDataUser.getUser();
        final Message message = MockDataMessage.getMessage();
        message.setAuthor(author);
        final MessageDto messageDto = messageConverter.convertToDto(message);

        assertEquals(messageDto.getMessage(), message.getMessage());
        assertEquals(messageDto.getAuthor(), message.getAuthor().getNickname());
    }

    @Test
    public void convertToDbo() {
        final String message = "message";
        final User author = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();
        author.setRoom(room);
        doReturn(author).when(userRepository).findByLogin(author.getLogin());
        final Message answer = messageConverter.convertToDbo(message,author.getLogin());

        assertEquals(author, answer.getAuthor());
        assertEquals(room, answer.getRoom());
        assertEquals(message, answer.getMessage());
    }
}