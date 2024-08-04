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

    //유저 뱃지 상태 업데이트
    @PutMapping("/user/{badgeId}")
    public ResponseEntity<BadgeDTO> updateUserBadgeStatus(@PathVariable Long badgeId, @RequestBody BadgeDTO badgeDTO) {
        BadgeDTO updatedUserBadge = badgeService.updateUserBadgeStatus(badgeId, badgeDTO.getIsAchieved());
        return ResponseEntity.ok(updatedUserBadge);
    }
}
