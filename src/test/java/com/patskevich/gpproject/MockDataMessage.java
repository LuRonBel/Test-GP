package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.MessageDto;
import com.patskevich.gpproject.entity.Message;

public class MockDataMessage {

    public static Message getMessage() {
        final Message message = new Message();
        message.setId(new Long(1));
        message.setMessage("message");
        message.setAuthor(null);
        message.setRoom(null);
        return message;
    }

    public static MessageDto getMessageOutputDto() {
        final MessageDto messageOutputDt = new MessageDto();
        messageOutputDt.setAuthor(null);
        messageOutputDt.setMessage("message");
        return messageOutputDt;
    }
}
