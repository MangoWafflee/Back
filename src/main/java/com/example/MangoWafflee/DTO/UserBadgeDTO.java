package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserBadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserBadgeDTO {
    private Long id;
    private StatusEnum isAchieved;
    private LocalDate achievedAt;
    private UserDTO user;
    private BadgeDTO badge;

    public static UserBadgeDTO entityToDto(UserBadgeEntity userBadgeEntity){
        return new UserBadgeDTO(
                userBadgeEntity.getId(),
                userBadgeEntity.getIsAchieved(),
                userBadgeEntity.getAchievedAt(),
                UserDTO.entityToDto(userBadgeEntity.getUser()),
                BadgeDTO.entityToDto(userBadgeEntity.getBadge())
        );
    }

    public UserBadgeEntity dtoToEntity(UserEntity user, BadgeEntity badge){
        return new UserBadgeEntity(id, isAchieved, achievedAt, user, badge);
    }
}
