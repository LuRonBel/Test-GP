package com.patskevich.gpproject.dto;

import com.patskevich.gpproject.entity.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserDtoUi {
    private String login;
    private String nickname;
    private String password;
    private Room room;
    private String role;
}
