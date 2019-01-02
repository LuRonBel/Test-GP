package com.patskevich.gpproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "LOG")
public class NicknameLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String login;

    @NotNull
    @Column(name="OLD_NICKNAME")
    private String oldNickname;

    @NotNull
    @Column(name="NEW_NICKNAME")
    private String newNickname;

    @NotNull
    private String date;
}
