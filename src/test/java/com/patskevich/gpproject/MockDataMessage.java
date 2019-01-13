package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.entity.Message;

import java.sql.Date;

public class MockDataMessage {

    public static Message getMessage() {
        final Message message = new Message();
        message.setId(1L);
        message.setMessage("message");
        message.setAuthor(null);
        message.setRoom(null);
        return message;
    }

    public static MessageDto getMessageDto() {
        final MessageDto messageOutputDt = new MessageDto();
        messageOutputDt.setAuthor(null);
        messageOutputDt.setMessage("message");
        return messageOutputDt;
    }
}
