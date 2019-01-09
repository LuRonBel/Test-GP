package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.NicknameLogDto;
import com.patskevich.gpproject.entity.NicknameLog;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class NicknameLogConverter {

    public NicknameLogDto convertToDto(final NicknameLog nicknameLog) {
        final NicknameLogDto nicknameLogDto = new NicknameLogDto();
        BeanUtils.copyProperties(nicknameLog, nicknameLogDto);
        return nicknameLogDto;
    }

    public NicknameLog convertToDbo(final NicknameLogDto nicknameLogDto) {
        final NicknameLog nicknameLog = new NicknameLog();
        BeanUtils.copyProperties(nicknameLogDto, nicknameLog);
        return nicknameLog;
    }
}
