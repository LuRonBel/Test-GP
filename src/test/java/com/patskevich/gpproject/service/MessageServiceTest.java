package com.patskevich.gpproject.service;

import com.patskevich.gpproject.MockDataMessage;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    
    @Test
    public void addMessage(){
        final String message = "message";
        final String login = "login";
        final String answer = messageService.addMessage(message, login);

        verify(messageConverter, times(1)).convertToDbo(message, login);
        verify(messageRepository, times(1)).save(null);
        assertEquals(LanguageMessage.getText("successfully"), answer);
    }

    @Test
    public void deleteMessage(){
        final Message message = MockDataMessage.getMessage();
        doReturn(true).when(messageRepository).existsById(message.getId());
        final String answer = messageService.deleteMessage(message.getId());

        verify(messageRepository, times(1)).deleteById(message.getId());
        assertEquals(LanguageMessage.getText("successfully"), answer);
    }

    @Test
    public void editMessage(){
        final User author = MockDataUser.getUser();
        final Message message = MockDataMessage.getMessage();
        final String newMessage = "new message";
        message.setAuthor(new User());
        doReturn(true).when(messageRepository).existsById(message.getId());
        doReturn(author).when(userRepository).findByLogin(author.getLogin());
        doReturn(message).when(messageRepository).getById(message.getId());
        String answer = messageService.editMessage(message.getId(), newMessage, author.getLogin());
        assertEquals(LanguageMessage.getText("access.error"), answer);

        message.setAuthor(author);
        answer = messageService.editMessage(message.getId(), newMessage, author.getLogin());
        assertEquals(newMessage+" | "+LanguageMessage.getText("edited.user"), message.getMessage());
        assertEquals(LanguageMessage.getText("successfully"), answer);

        message.setMessage("message");
        author.setRole(RoleEnum.ROLE_ADMIN.toString());
        answer = messageService.editMessage(message.getId(), newMessage, author.getLogin());
        assertEquals(newMessage+" | "+LanguageMessage.getText("edited.admin"), message.getMessage());
        assertEquals(LanguageMessage.getText("successfully"), answer);

        answer = messageService.editMessage(2L, newMessage, author.getLogin());
        assertEquals(LanguageMessage.getText("message.not.found"), answer);

        verify(messageRepository, times(3)).existsById(message.getId());
        verify(messageRepository, times(1)).existsById(2L);
        verify(userRepository, times(5)).findByLogin(author.getLogin());
        verify(messageRepository, times(4)).getById(message.getId());
        verify(messageRepository, times(2)).save(message);
    }
}
