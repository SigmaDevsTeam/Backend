package com.sigmadevs.testtask.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "is_true")
    private Boolean isTrue;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Task task;
}