package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeService {
    int calculateCompletedAttempts(UserEntity user, LocalDate participationDate);
    ChallengeDTO createChallenge(ChallengeDTO challengeDTO);
    List<ChallengeDTO> getAllChallenges();
    ChallengeDTO getChallengeById(Long challengeId);
    ChallengeDTO updateChallengeStatus(Long challengeId, StatusEnum status);
    UserChallengeDTO participateInChallenge(Long userId, Long challengeId, StatusEnum status);
    UserChallengeDTO updateUserChallengeStatus(Long userChallengeId, StatusEnum status);
    void checkAndUpdateChallengeStatus(Long userId);
    List<UserChallengeDTO> getUserChallenges(Long userId);
    List<ChallengeDTO> getOngoingChallenges();
    List<ChallengeDTO> getCompletedChallenges();
    List<ChallengeDTO> getPendingChallenges();
}
