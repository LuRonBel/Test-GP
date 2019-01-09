package com.patskevich.gpproject.dto.UserDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserDto{
    private String login;
    private String password;
}
