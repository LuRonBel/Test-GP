package com.patskevich.gpproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "NICKNAME_CHANGE_HISTORY")
public class NicknameChangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String oldNickname;

    @NotNull
    private String newNickname;

    @NotNull
    private Long userId;

    @NotNull
    private Date date;
}
