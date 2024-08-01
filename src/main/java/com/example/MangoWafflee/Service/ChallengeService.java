package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;

import java.util.List;

public interface ChallengeService {
    ChallengeDTO createChallenge(ChallengeDTO challengeDTO);
    List<ChallengeDTO> getAllChallenges();
    ChallengeDTO updateChallengeStatus(Long challengeId, StatusEnum status);
}
