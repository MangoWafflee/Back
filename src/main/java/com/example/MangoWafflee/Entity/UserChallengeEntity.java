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
    private int completedAttempts; // 챌린지를 시작한 이후부터의 스마일 카운트 값
    private LocalDate participationDate; // 챌린지 참여 날짜 (누적 방지를 위해)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private ChallengeEntity challenge;
}


