package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;

import java.util.List;

public interface ChallengeService {
    ChallengeDTO addChallenge(ChallengeDTO challengeDTO);
    List<ChallengeDTO> getChallenges();
    ChallengeDTO getChallengeById(Long id);
}
