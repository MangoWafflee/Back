package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.BadgeDTO;
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
    @PostMapping("/add/{userId}")
    public ResponseEntity<BadgeDTO> addBadge(@RequestBody BadgeDTO badgeDTO, @PathVariable Long userId) {
        BadgeDTO createdBadge = badgeService.addBadge(badgeDTO, userId);
        return ResponseEntity.ok(createdBadge);
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

    //해당 유저 전체 뱃지 조회
    @GetMapping("/user/{userId}")
    public List<BadgeDTO> getUserBadges(@PathVariable Long userId) {
        return badgeService.getUserBadges(userId);
    }

    //유저 뱃지 상태 업데이트
    @PutMapping("/user/{badgeId}")
    public ResponseEntity<BadgeDTO> updateUserBadgeStatus(@PathVariable Long badgeId, @RequestBody BadgeDTO badgeDTO) {
        BadgeDTO updatedUserBadge = badgeService.updateUserBadgeStatus(badgeId, badgeDTO.getIsAchieved());
        return ResponseEntity.ok(updatedUserBadge);
    }
}
