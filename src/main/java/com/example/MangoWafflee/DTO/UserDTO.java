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

    public static UserDTO entityToDto(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUid(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getNickname(),
                userEntity.getImage(),
                userEntity.getEmail(),
                userEntity.getProvider()
        );
    }

    public UserEntity dtoToEntity() {
        return UserEntity.builder()
                .id(id)
                .uid(uid)
                .password(password)
                .name(name)
                .nickname(nickname)
                .image(image)
                .email(email)
                .provider(provider)
                .build();
    }
}


