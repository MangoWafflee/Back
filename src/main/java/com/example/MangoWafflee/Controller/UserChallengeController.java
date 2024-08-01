package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Service.UserChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userchallenge")
@RequiredArgsConstructor
public class UserChallengeController {
    private final UserChallengeService userChallengeService;

    // 유저 챌린지 참여
    @PostMapping("/participate")
    public ResponseEntity<UserChallengeDTO> participateInChallenge(@RequestBody UserChallengeDTO userChallengeDTO) {
        UserChallengeDTO userChallenge = userChallengeService.participateInChallenge(userChallengeDTO.getUser().getId(), userChallengeDTO.getChallenge().getId(), userChallengeDTO.getParticipating());
        return ResponseEntity.ok(userChallenge);
    }

    // 유저 챌린지 성공 여부 업데이트
    @PutMapping("/status/{userChallengeId}")
    public ResponseEntity<UserChallengeDTO> updateUserChallengeStatus(@PathVariable Long userChallengeId, @RequestBody UserChallengeDTO userChallengeDTO) {
        UserChallengeDTO updatedUserChallenge = userChallengeService.updateUserChallengeStatus(userChallengeId, userChallengeDTO.getSuccessStatus());
        return ResponseEntity.ok(updatedUserChallenge);
    }
}
