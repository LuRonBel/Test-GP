package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.MockDataNicknameLog;
import com.patskevich.gpproject.dto.NicknameLogDto;
import com.patskevich.gpproject.entity.NicknameLog;
import org.junit.Test;

import static org.junit.Assert.*;

public class NicknameLogConverterTest {

    private NicknameLogConverter nicknameLogConverter = new NicknameLogConverter();

    @Test
    public void convertToDto() {

        final NicknameLog nicknameLog = MockDataNicknameLog.getNicknameLog();

        final NicknameLogDto nicknameLogDto = nicknameLogConverter.convertToDto(nicknameLog);

        assertEquals(nicknameLogDto.getLogin(), nicknameLog.getLogin());
        assertEquals(nicknameLogDto.getNewNickname(), nicknameLog.getNewNickname());
        assertEquals(nicknameLogDto.getOldNickname(), nicknameLog.getOldNickname());
        assertEquals(nicknameLogDto.getDate(), nicknameLog.getDate());
    }

    @Test
    public void convertToDbo() {

        final NicknameLogDto nicknameLogDto = MockDataNicknameLog.getNicknameLogDto();

        final NicknameLog nicknameLog = nicknameLogConverter.convertToDbo(nicknameLogDto);

        assertEquals(nicknameLogDto.getLogin(), nicknameLog.getLogin());
        assertEquals(nicknameLogDto.getNewNickname(), nicknameLog.getNewNickname());
        assertEquals(nicknameLogDto.getOldNickname(), nicknameLog.getOldNickname());
        assertEquals(nicknameLogDto.getDate(), nicknameLog.getDate());
    }
}