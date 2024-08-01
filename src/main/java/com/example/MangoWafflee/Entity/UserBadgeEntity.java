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
@Table(name = "userbadge")
public class UserBadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatusEnum isAchieved; // 개인 기록 달성 여부
    private LocalDate achievedAt; // 개인 기록 달성 시간

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private BadgeEntity badge;
}
