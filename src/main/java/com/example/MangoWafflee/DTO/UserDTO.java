package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.UserEntity;
import lombok.*;

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

    public static UserDTO entityToDto(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUid(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getNickname(),
                userEntity.getImage(),
                userEntity.getEmail(),
                userEntity.getProvider(),
                userEntity.getSmilecount()
        );
    }

    public UserEntity dtoToEntity() {
        return new UserEntity(id, uid, password, name, nickname, image, email, provider, smilecount);
    }
}

