package com.patskevich.gpproject.dto.NicknameChangeHistoryDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class NicknameChangeHistoryDto {
    private Long id;
    private String oldNickname;
    private String newNickname;
    private Long userId;
    private Date date;
}
