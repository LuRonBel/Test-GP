package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Service
public class MessageConverter {

    private final UserRepository userRepository;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public MessageOutputDto convertToDto(final Message message) {
        final MessageOutputDto messageOutputDto= new MessageOutputDto();
        messageOutputDto.setMessage(message.getMessage());
        messageOutputDto.setAuthor(message.getAuthor().getNickname());
        messageOutputDto.setDate(message.getDate());
        return messageOutputDto;
    }

    public Message convertToDbo(final MessageInputDto messageInputDto, final String login) {
        final Message message = new Message();
        message.setAuthor(userRepository.findByLogin(login));
        message.setRoom(userRepository.findByLogin(login).getRoom());
        message.setMessage(messageInputDto.getMessage());
        message.setDate(simpleDateFormat.format(new Date()));
        return message;
    }

    public Message convertToDbo(final String messageText,final String login) {
        final Message message = new Message();
        message.setAuthor(userRepository.findByLogin(login));
        message.setRoom(userRepository.findByLogin(login).getRoom());
        message.setMessage(messageText);
        message.setDate(simpleDateFormat.format(new Date()));
        return message;
    }
}
