package com.patskevich.gpproject.dto.UserDto;

import com.patskevich.gpproject.entity.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String login;
    private String nickname;
    private Room room;
    private String role;
}
