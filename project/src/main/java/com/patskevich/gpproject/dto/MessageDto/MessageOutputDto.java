package com.patskevich.gpproject.dto.MessageDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageOutputDto {
    private String message;
    private String author;
    private String date;
}
