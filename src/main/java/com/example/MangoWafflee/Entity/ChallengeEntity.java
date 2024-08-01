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
@Table(name = "challenges")
public class ChallengeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean participating;
    private Boolean successStatus;
    private int participantCount;
    private int totalAttempts;
    private int completedAttempts;
}
