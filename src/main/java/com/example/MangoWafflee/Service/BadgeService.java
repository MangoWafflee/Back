package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;

import java.util.List;

public interface BadgeService {
    BadgeDTO addBadge(BadgeDTO badgeDTO);
    List<BadgeDTO> getBadges();
    BadgeDTO getBadgeById(Long id);
}
