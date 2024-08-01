package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    //챌린지 생성
    @PostMapping
    public ChallengeDTO addChallenge(@RequestBody ChallengeDTO challengeDTO) {
        return challengeService.addChallenge(challengeDTO);
    }

    //챌린지 전체 조회
    @GetMapping
    public List<ChallengeDTO> getChallenges() {
        return challengeService.getChallenges();
    }

    //해당 챌린지 조회
    @GetMapping("/{id}")
    public ChallengeDTO getChallengeById(@PathVariable Long id) {
        return challengeService.getChallengeById(id);
    }
}
