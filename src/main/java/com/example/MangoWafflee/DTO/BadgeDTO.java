package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BadgeDTO {
    private Long id;
    private String title;
    private StatusEnum isAchieved; // 개인 기록 달성 여부
    private LocalDate achievedAt; // 개인 기록 달성 시간
    private int requiredSmileCount;

    public static BadgeDTO entityToDto(BadgeEntity badgeEntity) {
        return new BadgeDTO(
                badgeEntity.getId(),
                badgeEntity.getTitle(),
                badgeEntity.getIsAchieved(),
                badgeEntity.getAchievedAt(),
                badgeEntity.getRequiredSmileCount()
        );
    }

    public BadgeEntity dtoToEntity() {
        return new BadgeEntity(id, title, isAchieved, achievedAt, requiredSmileCount, null);
    }
}
