package com.sigmadevs.testtask.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "room")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @NotNull
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @OneToMany(mappedBy = "room")
    private List<User> users;
    private String username;
}