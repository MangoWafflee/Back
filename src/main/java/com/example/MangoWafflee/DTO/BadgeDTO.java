package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.BadgeEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BadgeDTO {
    private Long id;
    private String title;

    public static BadgeDTO entityToDto(BadgeEntity badgeEntity) {
        return new BadgeDTO(
                badgeEntity.getId(),
                badgeEntity.getTitle()
        );
    }

    public BadgeEntity dtoToEntity() {
        return new BadgeEntity(id, title);
    }
}
