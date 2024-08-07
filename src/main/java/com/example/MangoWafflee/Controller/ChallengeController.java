package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    //해당 챌린지 조회
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeDTO> getChallengeById(@PathVariable Long challengeId) {
        ChallengeDTO challenge = challengeService.getChallengeById(challengeId);
        return ResponseEntity.ok(challenge);
    }

    //챌린지 상태 업데이트
    @PutMapping("/status/{challengeId}")
    public ResponseEntity<ChallengeDTO> updateChallengeStatus(@PathVariable Long challengeId, @RequestBody ChallengeDTO challengeDTO) {
        ChallengeDTO updatedChallenge = challengeService.updateChallengeStatus(challengeId, challengeDTO.getStatus());
        return ResponseEntity.ok(updatedChallenge);
    }

    //유저 챌린지 참여
    @PostMapping("/userchallenge/participate")
    public ResponseEntity<UserChallengeDTO> participateInChallenge(@RequestBody UserChallengeDTO userChallengeDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserChallengeDTO userChallenge = challengeService.participateInChallenge(userChallengeDTO.getUser().getId(), userChallengeDTO.getChallenge().getId(), userChallengeDTO.getParticipating(), userDetails);
        return ResponseEntity.ok(userChallenge);
    }

    //유저 챌린지 성공 여부
    @PutMapping("/userchallenge/status/{userChallengeId}")
    public ResponseEntity<UserChallengeDTO> updateUserChallengeStatus(@PathVariable Long userChallengeId, @RequestBody UserChallengeDTO userChallengeDTO) {
        UserChallengeDTO updatedUserChallenge = challengeService.updateUserChallengeStatus(userChallengeId, userChallengeDTO.getSuccessStatus());
        return ResponseEntity.ok(updatedUserChallenge);
    }

    //유저 챌린지 조회
    @GetMapping("/userchallenge/{userId}")
    public ResponseEntity<List<UserChallengeDTO>> getUserChallenges(@PathVariable Long userId) {
        List<UserChallengeDTO> userChallenges = challengeService.getUserChallenges(userId);
        return ResponseEntity.ok(userChallenges);
    }

    //진행중인 챌린지만 조회
    @GetMapping("/going")
    public ResponseEntity<List<ChallengeDTO>> getOngoingChallenges() {
        List<ChallengeDTO> ongoingChallenges = challengeService.getOngoingChallenges();
        return ResponseEntity.ok(ongoingChallenges);
    }

    //진행완료인 챌린지만 조회
    @GetMapping("/complete")
    public ResponseEntity<List<ChallengeDTO>> getCompletedChallenges() {
        List<ChallengeDTO> completedChallenges = challengeService.getCompletedChallenges();
        return ResponseEntity.ok(completedChallenges);
    }

    //대기중인 챌린지만 조회
    @GetMapping("/pending")
    public ResponseEntity<List<ChallengeDTO>> getPendingChallenges() {
        List<ChallengeDTO> pendingChallenges = challengeService.getPendingChallenges();
        return ResponseEntity.ok(pendingChallenges);
    }
}
