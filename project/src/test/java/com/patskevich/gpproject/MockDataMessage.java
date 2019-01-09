package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.MessageDto.MessageCorrectDto;
import com.patskevich.gpproject.dto.MessageDto.MessageInputDto;
import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
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

    public static MessageCorrectDto getMessageCorrectDto() {
        final MessageCorrectDto messageCorrectDto = new MessageCorrectDto();
        messageCorrectDto.setId(new Long(1));
        messageCorrectDto.setMessage("new message");
        return messageCorrectDto;
    }

    public static MessageInputDto getMessageInputDto() {
        final MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setMessage("message");
        return messageInputDto;
    }

    public static MessageOutputDto getMessageOutputDto() {
        final MessageOutputDto messageOutputDt = new MessageOutputDto();
        messageOutputDt.setAuthor(null);
        messageOutputDt.setMessage("message");
        return messageOutputDt;
    }
}
