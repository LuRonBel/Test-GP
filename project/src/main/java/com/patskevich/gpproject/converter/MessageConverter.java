package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageConverter {

    private final UserRepository userRepository;

    public MessageOutputDto convertToDto(final Message message) {
        final MessageOutputDto messageOutputDto= new MessageOutputDto();
        messageOutputDto.setMessage(message.getMessage());
        messageOutputDto.setAuthor(message.getAuthor().getName());
        return messageOutputDto;
    }

    public Message convertToDbo(final MessageInputDto messageInputDto) {
        final Message message = new Message();
        message.setAuthor(userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()));
        message.setRoom(userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .getRoom());
        message.setMessage(messageInputDto.getMessage());
        return message;
    }

    public Message convertToDbo(final String messageText) {
        final Message message = new Message();
        message.setAuthor(userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()));
        message.setRoom(userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .getRoom());
        message.setMessage(messageText);
        return message;
    }
}
