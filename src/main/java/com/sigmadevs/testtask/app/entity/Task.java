package com.sigmadevs.testtask.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "task")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String audio;
    private String video;
    private String image;
    private String openAnswer;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @OneToMany(mappedBy = "task", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Option> options;
}
