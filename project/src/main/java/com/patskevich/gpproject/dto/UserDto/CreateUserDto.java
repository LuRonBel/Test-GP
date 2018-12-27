package com.patskevich.gpproject.dto.UserDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserDto{
    private Long id;
    private String name;
    private String password;
}
