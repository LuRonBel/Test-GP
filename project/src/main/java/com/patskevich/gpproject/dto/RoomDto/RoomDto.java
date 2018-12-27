package com.patskevich.gpproject.dto.RoomDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDto {
    private long id;
    private String name;
    private String description;
}
