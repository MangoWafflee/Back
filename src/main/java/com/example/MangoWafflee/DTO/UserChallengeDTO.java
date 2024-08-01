package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Entity.UserChallengeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserChallengeDTO {
    private Long id;
    private StatusEnum participating;
    private StatusEnum successStatus;
    private UserDTO user;
    private ChallengeDTO challenge;

    public static UserChallengeDTO entityToDto(UserChallengeEntity userChallengeEntity) {
        return new UserChallengeDTO(
                userChallengeEntity.getId(),
                userChallengeEntity.getParticipating(),
                userChallengeEntity.getSuccessStatus(),
                UserDTO.entityToDto(userChallengeEntity.getUser()),
                ChallengeDTO.entityToDto(userChallengeEntity.getChallenge())
        );
    }

    public UserChallengeEntity dtoToEntity(UserEntity user, ChallengeEntity challenge) {
        return new UserChallengeEntity(id, participating, successStatus, user, challenge);
    }
}
