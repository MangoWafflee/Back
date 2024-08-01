package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;

import java.util.List;

public interface BadgeService {
    List<BadgeDTO> getBadgesByUserId(Long userId);
    BadgeDTO addBadge(BadgeDTO badgeDTO);
}
