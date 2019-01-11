package com.patskevich.gpproject.dto.UserDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String login;
    private String nickname;
    private String room;
    private String role;
}
