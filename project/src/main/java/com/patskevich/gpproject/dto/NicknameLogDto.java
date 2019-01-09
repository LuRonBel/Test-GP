package com.patskevich.gpproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class NicknameLogDto {
    private String login;
    private String oldNickname;
    private String newNickname;
    private String date;
}
