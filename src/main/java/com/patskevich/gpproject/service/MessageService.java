package com.patskevich.gpproject.service;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.MessageConverter;
import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.repository.MessageRepository;
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

    public String addMessage(final String message, final String login) {
        messageRepository.save(messageConverter.convertToDbo(message, login));
        return LanguageMessage.getText("successfully");
    }

    public List<MessageDto> getMessage(final String login) {
        return messageRepository.findAllByRoomOrderByIdDesc(userRepository.findByLogin(login).getRoom())
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public String deleteMessage(final Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return LanguageMessage.getText("successfully");
        } else return LanguageMessage.getText("message.not.found");
    }

    public String editMessage(final Long id, final String newMessage, final String login) {
        if (messageRepository.existsById(id)) {
            if (userRepository
                    .findByLogin(login)
                    .getRole()
                    .equals(RoleEnum.ROLE_ADMIN.toString())) {
                final Message message = messageRepository.getById(id);
                message.setMessage(newMessage+" | "+LanguageMessage.getText("edited.admin"));
                messageRepository.save(message);
                return LanguageMessage.getText("successfully");
            } else {
                if (messageRepository.getById(id).getAuthor()
                        .equals(userRepository.findByLogin(login))) {
                    final Message message = messageRepository.getById(id);
                    message.setMessage(newMessage+" | "+LanguageMessage.getText("edited.user"));
                    messageRepository.save(message);
                    return LanguageMessage.getText("successfully");
                } else return LanguageMessage.getText("access.error");
            }
        } else return LanguageMessage.getText("message.not.found");
    }

    public List<MessageDto> findMessages(final String fromDate, final String toDate, final String login) {
        final Date from = Date.valueOf(LocalDate.parse(fromDate).plusDays(1));
        final Date to = Date.valueOf(LocalDate.parse(toDate).plusDays(1));
        return messageRepository.findByRoomAndDateBetween(userRepository.findByLogin(login).getRoom(), from, to)
                .stream()
                .map(messageConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
