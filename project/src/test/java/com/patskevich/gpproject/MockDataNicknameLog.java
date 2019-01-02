package com.patskevich.gpproject;

import com.patskevich.gpproject.dto.NicknameLogDto;
import com.patskevich.gpproject.entity.NicknameLog;

public class MockDataNicknameLog {

    public static NicknameLog getNicknameLog() {
        final NicknameLog nicknameLog = new NicknameLog();
        nicknameLog.setId(new Long(1));
        nicknameLog.setLogin("login");
        nicknameLog.setOldNickname("old nickname");
        nicknameLog.setNewNickname("new nickname");
        nicknameLog.setDate("01.01.2019");
        return nicknameLog;
    }

    public static NicknameLogDto getNicknameLogDto() {
        final NicknameLogDto nicknameLogDto = new NicknameLogDto();
        nicknameLogDto.setLogin("login");
        nicknameLogDto.setOldNickname("old nickname");
        nicknameLogDto.setNewNickname("new nickname");
        nicknameLogDto.setDate("01.01.2019");
        return nicknameLogDto;
    }
}
