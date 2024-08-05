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
    private String subTitle; // 챌린지 부제목
    private String content; // 챌린지 내용
    private LocalDate startDate; // 챌린지 시작 시간
    private LocalDate endDate; // 챌린지 종료 시간
    private StatusEnum status; // 챌린지 상태
    private int count; // 챌린지 참여자 수
    private int totalAttempts; // 챌린지 완료까지 성공해야할 횟수
    private int completedAttempts; // 챌린지 성공 횟수
    private String img; // 챌린지 이미지 (GCP 연결 없이 수동으로 URL 채울 예정)
}
