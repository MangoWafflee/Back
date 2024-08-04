package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String uid;
    private String password;
    private String name;
    private String nickname;
    private String image;
    private String email;
    private String provider;
    private int smilecount;
    private List<BadgeDTO> badges;

    public static UserDTO entityToDto(UserEntity userEntity) {
        List<BadgeDTO> badgeDTOs = userEntity.getBadges() != null
                ? userEntity.getBadges().stream().map(BadgeDTO::entityToDto).collect(Collectors.toList())
                : Collections.emptyList();
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUid(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getNickname(),
                userEntity.getImage(),
                userEntity.getEmail(),
                userEntity.getProvider(),
                userEntity.getSmilecount(),
                badgeDTOs
        );
    }

    public UserEntity dtoToEntity() {
        UserEntity userEntity = new UserEntity(id, uid, password, name, nickname, image, email, provider, smilecount);
        List<BadgeEntity> badgeEntities = badges != null
                ? badges.stream().map(BadgeDTO::dtoToEntity).collect(Collectors.toList())
                : Collections.emptyList();
        userEntity.setBadges(badgeEntities);
        return userEntity;
    }
}
