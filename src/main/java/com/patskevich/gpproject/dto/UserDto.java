package com.patskevich.gpproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    public static final String ROOT = "root";
    public static final String NEW_NICKNAME = "New user";
    private String login;
    private String nickname;
    private String room;
    private String role;
}
