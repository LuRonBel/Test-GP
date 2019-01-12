package com.patskevich.gpproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class MessageDto {
    private String message;
    private String author;
    private Date date;
}
