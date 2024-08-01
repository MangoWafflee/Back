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
    private String content;

    public static BadgeDTO entityToDto(BadgeEntity badgeEntity) {
        return new BadgeDTO(
                badgeEntity.getId(),
                badgeEntity.getTitle(),
                badgeEntity.getContent()
        );
    }

    public BadgeEntity dtoToEntity() {
        return new BadgeEntity(id, title, content);
    }
}
