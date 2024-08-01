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

    @GetMapping("/{userId}")
    public List<BadgeDTO> getBadgesByUser(@PathVariable Long userId) {
        return badgeService.getBadgesByUserId(userId);
    }

    @PostMapping
    public BadgeDTO addBadge(@RequestBody BadgeDTO badgeDTO) {
        return badgeService.addBadge(badgeDTO);
    }
}
