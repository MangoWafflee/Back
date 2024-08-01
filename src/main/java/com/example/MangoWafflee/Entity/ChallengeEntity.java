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
@Table(name = "challenges")
public class ChallengeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // 챌린지 제목
    private String content; // 챌린지 내용
    private LocalDate startDate; // 챌린지 시작 시간
    private LocalDate endDate; // 챌린지 종료 시간
    private StatusEnum status; // 챌린지 상태
    private int count; // 챌린지 참여자 수
    private int totalAttempts; // 챌린지 완료까지 수행할 횟수(?) => 총 시도 횟수?
    private int completedAttempts; // 챌린지 성공 횟수
}
