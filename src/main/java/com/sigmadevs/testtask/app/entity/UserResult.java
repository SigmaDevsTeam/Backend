package com.sigmadevs.testtask.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @Column(nullable = false, columnDefinition = "int default 0")
    @Builder.Default
    private Integer result = 0;
}




