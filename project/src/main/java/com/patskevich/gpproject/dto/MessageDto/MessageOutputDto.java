package com.patskevich.gpproject.dto.MessageDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

@Data
@NoArgsConstructor
public class MessageOutputDto {
    private String message;
    private String author;
    private Date date;
}
