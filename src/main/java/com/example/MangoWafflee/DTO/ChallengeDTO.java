package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
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
    private String subTitle;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private StatusEnum status;
    private int count;
    private int totalAttempts;
    private int completedAttempts;
    private String img;

    public static ChallengeDTO entityToDto(ChallengeEntity challengeEntity) {
        return new ChallengeDTO(
                challengeEntity.getId(),
                challengeEntity.getTitle(),
                challengeEntity.getSubTitle(),
                challengeEntity.getContent(),
                challengeEntity.getStartDate(),
                challengeEntity.getEndDate(),
                challengeEntity.getStatus(),
                challengeEntity.getCount(),
                challengeEntity.getTotalAttempts(),
                challengeEntity.getCompletedAttempts(),
                challengeEntity.getImg()
        );
    }

    public ChallengeEntity dtoToEntity() {
        return new ChallengeEntity(id, title, subTitle, content, startDate, endDate, status, count, totalAttempts, completedAttempts, img);
    }
}
