package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;

import java.util.List;

public interface ChallengeService {
    ChallengeDTO createChallenge(ChallengeDTO challengeDTO);
    List<ChallengeDTO> getAllChallenges();
    ChallengeDTO updateChallengeStatus(Long challengeId, StatusEnum status);
    UserChallengeDTO participateInChallenge(Long userId, Long challengeId, StatusEnum status);
    UserChallengeDTO updateUserChallengeStatus(Long userChallengeId, StatusEnum status);
}
