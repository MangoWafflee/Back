package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;

public interface UserChallengeService {
    UserChallengeDTO participateInChallenge(Long userId, Long challengeId, StatusEnum status);
    UserChallengeDTO updateUserChallengeStatus(Long userChallengeId, StatusEnum status);
}
