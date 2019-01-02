package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageRoomDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageConverter messageConverter;
    private final RoomRepository roomRepository;

    public String addMessage(final MessageInputDto messageInputDto) {
        messageRepository.save(messageConverter.convertToDbo(messageInputDto));
        return "Сообщение было создано в комнате " + userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .getRoom()
                .getName() + ".";
    }

    public String addMessage(final String message) {
        messageRepository.save(messageConverter.convertToDbo(message));
        return "Сообщение было создано в комнате " + userRepository
                .findByName(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .getRoom()
                .getName() + ".";
    }

    public List<MessageOutputDto> getRoomMesage(final MessageRoomDto messageRoomDto) {
        return messageRepository.findAllByRoomOrderByIdDesc(roomRepository.findByName(messageRoomDto.getName()))
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MessageOutputDto> getRoomMesage(final String name) {
        return messageRepository.findAllByRoomOrderByIdDesc(roomRepository.findByName(name))
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public String deleteMessage(final Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return "Сообщение с ID №" + id + " было удалено!";
        }
        return "Такого сообщения не существует!";
    }

    public String editMessage(final Long id, final MessageInputDto messageInputDto) {
        if (messageRepository.existsById(id)) {
            if (userRepository
                    .findByName(SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName())
                    .getRole()
                    .equals("ROLE_ADMIN")) {
                messageInputDto.setId(id);
                messageInputDto.setDate(messageRepository.findById(id).get().getDate());
                messageRepository.save(messageConverter.convertToDbo(messageInputDto));
                return "Сообщение было отредактировано администратором!";
            } else {
                if (messageRepository.findById(id).get().getAuthor().getId()
                        .equals(
                                userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).getId())) {
                    messageInputDto.setId(id);
                    messageInputDto.setDate(messageRepository.findById(id).get().getDate());
                    messageRepository.save(messageConverter.convertToDbo(messageInputDto));
                    return "Сообщение было отредактировано!";
                } else {
                    return "Вы не можете отредактировать чужое сообщение";
                }
            }
        }
        return "Ошибка при редактировании сообщения!";
    }

    public List<MessageOutputDto> findMessages(final MessageRoomDto roomDto, final String fromDate, final String toDate) {
        final Date from = Date.valueOf(LocalDate.parse(fromDate).plusDays(1));
        final Date to = Date.valueOf(LocalDate.parse(toDate).plusDays(1));
        return messageRepository.findByRoomAndDateBetween(roomRepository.findByName(roomDto.getName()), from, to)
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
