package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.UserBadgeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Service.UserBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userbadge")
@RequiredArgsConstructor
public class UserBadgeController {
    private final UserBadgeService userBadgeService;

    //유저 뱃지 생성
    @PostMapping
    public UserBadgeDTO createUserBadge(@RequestBody UserBadgeDTO userBadgeDTO) {
        return userBadgeService.createUserBadge(userBadgeDTO);
    }

    //해당 유저 전체 뱃지 조회
    @GetMapping("/{userId}")
    public List<UserBadgeDTO> getUserBadges(@PathVariable Long userId) {
        return userBadgeService.getUserBadges(userId);
    }

    //유저 뱃지 상태 업데이트
    @PutMapping("/{userBadgeId}")
    public ResponseEntity<UserBadgeDTO> updateUserBadgeStatus(@PathVariable Long userBadgeId, @RequestBody UserBadgeDTO userBadgeDTO) {
        UserBadgeDTO updatedUserBadge = userBadgeService.updateUserBadgeStatus(userBadgeId, userBadgeDTO.getIsAchieved());
        return ResponseEntity.ok(updatedUserBadge);
    }
}
