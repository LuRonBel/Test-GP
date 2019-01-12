package com.patskevich.gpproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDto {

    public static final String DEFAULT_ROOM = "Default room";
    private String name;
    private String description;
}
