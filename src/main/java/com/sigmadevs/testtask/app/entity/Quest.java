package com.sigmadevs.testtask.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "quest")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    private String image;
    private Integer taskCount;
    private Time timeLimit;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Integer usersRated;
    private Float rating;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments;
}