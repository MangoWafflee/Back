package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChallengeDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean participating;
    private Boolean successStatus;
    private int participantCount;
    private int totalAttempts;
    private int completedAttempts;

    public static ChallengeDTO entityToDto(ChallengeEntity challengeEntity) {
        return new ChallengeDTO(
                challengeEntity.getId(),
                challengeEntity.getTitle(),
                challengeEntity.getContent(),
                challengeEntity.getStartDate(),
                challengeEntity.getEndDate(),
                challengeEntity.isParticipating(),
                challengeEntity.getSuccessStatus(),
                challengeEntity.getParticipantCount(),
                challengeEntity.getTotalAttempts(),
                challengeEntity.getCompletedAttempts()
        );
    }

    public ChallengeEntity dtoToEntity() {
        return new ChallengeEntity(id, title, content, startDate, endDate, participating, successStatus, participantCount, totalAttempts, completedAttempts);
    }
}
