package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    //챌린지 생성
    @PostMapping
    public ResponseEntity<ChallengeDTO> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        ChallengeDTO createdChallenge = challengeService.createChallenge(challengeDTO);
        return ResponseEntity.ok(createdChallenge);
    }

    //챌린지 조회
    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges);
    }

    //챌린지 상태 업데이트
    @PutMapping("/status/{challengeId}")
    public ResponseEntity<ChallengeDTO> updateChallengeStatus(@PathVariable Long challengeId, @RequestBody ChallengeDTO challengeDTO) {
        ChallengeDTO updatedChallenge = challengeService.updateChallengeStatus(challengeId, challengeDTO.getStatus());
        return ResponseEntity.ok(updatedChallenge);
    }
}
