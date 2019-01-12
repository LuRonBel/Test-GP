package com.patskevich.gpproject.converter;

import com.patskevich.gpproject.dto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.entity.NicknameChangeHistory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class NicknameChangeHistoryConverter {

    public NicknameChangeHistoryDto convertToDto(final NicknameChangeHistory nicknameChangeHistory) {
        final NicknameChangeHistoryDto nicknameChangeHistoryDto = new NicknameChangeHistoryDto();
        BeanUtils.copyProperties(nicknameChangeHistory, nicknameChangeHistoryDto);
        return nicknameChangeHistoryDto;
    }

    public NicknameChangeHistory convertToDbo(final NicknameChangeHistoryDto nicknameChangeHistoryDto) {
        final NicknameChangeHistory nicknameChangeHistory = new NicknameChangeHistory();
        BeanUtils.copyProperties(nicknameChangeHistoryDto, nicknameChangeHistory);
        return nicknameChangeHistory;
    }
}
