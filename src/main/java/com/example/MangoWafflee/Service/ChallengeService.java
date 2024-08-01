package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;

import java.util.List;

public interface ChallengeService {
    List<ChallengeDTO> getChallengesByUserId(Long userId);
    ChallengeDTO addChallenge(ChallengeDTO challengeDTO);
}
