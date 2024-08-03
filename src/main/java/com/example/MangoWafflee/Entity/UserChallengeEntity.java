package com.example.MangoWafflee.Entity;

import com.example.MangoWafflee.Enum.StatusEnum;
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
@Table(name = "userchallenge")
public class UserChallengeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatusEnum participating; // 챌린지 참여 여부
    private StatusEnum successStatus; // 챌린지 성공 여부
    private int completedAttempts; // 챌린지 해당 유저 수행한 횟수

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private ChallengeEntity challenge;
}


