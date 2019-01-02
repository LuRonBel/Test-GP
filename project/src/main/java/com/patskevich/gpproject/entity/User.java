package com.patskevich.gpproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private Room room;

    @NotNull
    private String role;
}
