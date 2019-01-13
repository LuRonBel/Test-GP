package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageConverter {

    private final UserRepository userRepository;

    public MessageDto convertToDto(final Message message) {
        final MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message.getMessage());
        messageDto.setAuthor(message.getAuthor().getNickname());
        messageDto.setDate(message.getDate());
        return messageDto;
    }

    public Message convertToDbo(final String messageText, final String login) {
        final Message message = new Message();
        message.setAuthor(userRepository.findByLogin(login));
        message.setRoom(userRepository.findByLogin(login).getRoom());
        message.setMessage(messageText);
        return message;
    }
}
