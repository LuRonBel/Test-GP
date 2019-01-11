package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.dto.MessageDto.MessageCorrectDto;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageConverter messageConverter;
    private final RoomRepository roomRepository;

    public String addMessage(final MessageInputDto messageInputDto, final String login) {
        messageRepository.save(messageConverter.convertToDbo(messageInputDto, login));
        return "Сообщение было создано в комнате "+userRepository
                .findByLogin(login)
                .getRoom()
                .getName()+".";
    }

    public String addMessage(final String message, final String login) {
        messageRepository.save(messageConverter.convertToDbo(message, login));
        return "Сообщение было создано в комнате "+userRepository
                .findByLogin(login)
                .getRoom()
                .getName()+".";
    }

    public List<MessageOutputDto> getRoomMessage(final String login) {
        return messageRepository.findAllByRoomOrderByIdDesc(userRepository.findByLogin(login).getRoom())
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public String deleteMessage(final Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return "Сообщение с ID №" + id + " было удалено!";
        } else return "Cообщения с ID № "+ id +" не существует!";
    }

    public String editMessage(final MessageCorrectDto messageCorrectDto, final String login) {
        if (messageRepository.existsById(messageCorrectDto.getId())) {
            if (userRepository
                    .findByLogin(login)
                    .getRole()
                    .equals("ROLE_ADMIN")) {
                final Message message = messageRepository.getById(messageCorrectDto.getId());
                message.setMessage(messageCorrectDto.getMessage()+" | edited by admin");
                messageRepository.save(message);
                return "Сообщение было отредактировано администратором!";
            } else {
                if (messageRepository.getById(messageCorrectDto.getId()).getAuthor()
                        .equals(
                                userRepository.findByLogin(login))) {
                    final Message message = messageRepository.getById(messageCorrectDto.getId());
                    message.setMessage(messageCorrectDto.getMessage()+" | edited");
                    messageRepository.save(message);
                    return "Сообщение было отредактировано!";
                } else {
                    return "Вы не можете отредактировать чужое сообщение";
                }
            }
        }
        return "Ошибка при редактировании сообщения!";
    }

    public List<MessageOutputDto> findMessages(final String fromDate, final String toDate, final String login) {
        final Date from = Date.valueOf(LocalDate.parse(fromDate).plusDays(1));
        final Date to = Date.valueOf(LocalDate.parse(toDate).plusDays(1));
        return messageRepository.findByRoomAndDateBetween(userRepository.findByLogin(login).getRoom(), from, to)
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
