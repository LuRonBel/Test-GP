package com.patskevich.gpproject.dto.MessageDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.sql.Date;

@Data
@NoArgsConstructor
public class MessageInputDto{
    private Long id;
    private String message;
    private Date date;
}
