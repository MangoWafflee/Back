package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Service.ChallengeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeServiceImpl challengeService;

    @GetMapping("/{userId}")
    public List<ChallengeDTO> getChallengesByUser(@PathVariable Long userId) {
        return challengeService.getChallengesByUserId(userId);
    }

    @PostMapping
    public ChallengeDTO addChallenge(@RequestBody ChallengeDTO challengeDTO) {
        return challengeService.addChallenge(challengeDTO);
    }
}
