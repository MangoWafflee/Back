package com.example.MangoWafflee.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "badges")
public class BadgeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Long count;
    private boolean isAchieved;
    private LocalDate achievedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
