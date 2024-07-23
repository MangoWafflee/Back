package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.UserEntity;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String kakaoId;

    public static UserDTO entityToDTO(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getKakaoId()
        );
    }

    public UserEntity dtoToEntity() {
        return new UserEntity(id, username, email, kakaoId);
    }
}
