package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.Service.BadgeService;
import lombok.RequiredArgsConstructor;
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
}
