package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.dto.MessageDto.MessageCorrectDto;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.repository.MessageRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Deprecated
    public List<MessageOutputDto> getRoomMessage(final NameRoomDto nameRoomDto) {
        return messageRepository.findAllByRoomOrderByIdDesc(roomRepository.findByName(nameRoomDto.getName()))
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
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

    public String correctMessage(MessageCorrectDto messageCorrectDto, final String login) {
        if (messageRepository.existsById(messageCorrectDto.getId())) {
            final Message message = messageRepository.getById(messageCorrectDto.getId());
            if (userRepository.findByLogin(login).getRole().equals("ROLE_ADMIN")) {
                message.setMessage(messageCorrectDto.getMessage() + " | ОТРЕДАКТИРОВАНО АДМИНИСТРАТОРОМ");
                messageRepository.save(message);
                return "Сообщение с ID № " + messageCorrectDto.getId() + " было отредактировано!";
            } else if (message.getAuthor().getLogin().equals(login)) {
                message.setMessage(messageCorrectDto.getMessage() + " | ОТРЕДАКТИРОВАНО");
                messageRepository.save(message);
                return "Сообщение с ID № " + messageCorrectDto.getId() + " было отредактировано!";
            } else return "У вас недостаточно прав";
        } else return "Такого сообщения не существует!";
    }

    public List<MessageOutputDto> getMessageByDate(final String login, final String dateFrom, final String dateTo){
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final List<MessageOutputDto> list = messageRepository.findAllByRoomOrderByIdDesc(userRepository.findByLogin(login).getRoom())
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
        final List<MessageOutputDto> newList = new ArrayList<>();
        for (MessageOutputDto message: list) {
            try {
                if ( format.parse(message.getDate()).after(format.parse(dateFrom)) &&
                        format.parse(message.getDate()).before(format.parse(dateTo))  ) {
                    newList.add(message);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newList;
    }
}
