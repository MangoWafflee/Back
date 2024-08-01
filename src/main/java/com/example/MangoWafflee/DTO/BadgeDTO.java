package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
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
    private String content;
    private Long count;
    private boolean isAchieved;
    private LocalDate achievedAt;

    public static BadgeDTO entityToDto(BadgeEntity badgeEntity) {
        return new BadgeDTO(
                badgeEntity.getId(),
                badgeEntity.getTitle(),
                badgeEntity.getContent(),
                badgeEntity.getCount(),
                badgeEntity.isAchieved(),
                badgeEntity.getAchievedAt()
        );
    }

    public BadgeEntity dtoToEntity() {
        return new BadgeEntity(id, title, content, count, isAchieved, achievedAt);
    }
}
