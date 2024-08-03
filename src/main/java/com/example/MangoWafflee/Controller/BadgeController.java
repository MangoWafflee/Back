package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.DTO.UserBadgeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/badge")
@RequiredArgsConstructor
public class BadgeController {
    private final BadgeService badgeService;

    //뱃지 생성
    @PostMapping
    public BadgeDTO addBadge(@RequestBody BadgeDTO badgeDTO) {
        return badgeService.addBadge(badgeDTO);
    }

    //뱃지 전체 조회
    @GetMapping
    public List<BadgeDTO> getBadges() {
        return badgeService.getBadges();
    }

    //해당 뱃지 조회
    @GetMapping("/{id}")
    public BadgeDTO getBadgeById(@PathVariable Long id) {
        return badgeService.getBadgeById(id);
    }

    //유저 뱃지 생성
    @PostMapping("/userbadge")
    public UserBadgeDTO createUserBadge(@RequestBody UserBadgeDTO userBadgeDTO) {
        return badgeService.createUserBadge(userBadgeDTO);
    }

    //해당 유저 전체 뱃지 조회
    @GetMapping("/userbadges/{userId}")
    public List<UserBadgeDTO> getUserBadges(@PathVariable Long userId) {
        return badgeService.getUserBadges(userId);
    }

    //유저 뱃지 상태 업데이트
    @PutMapping("/userbadge/{userBadgeId}")
    public ResponseEntity<UserBadgeDTO> updateUserBadgeStatus(@PathVariable Long userBadgeId, @RequestBody UserBadgeDTO userBadgeDTO) {
        UserBadgeDTO updatedUserBadge = badgeService.updateUserBadgeStatus(userBadgeId, userBadgeDTO.getIsAchieved());
        return ResponseEntity.ok(updatedUserBadge);
    }
}
