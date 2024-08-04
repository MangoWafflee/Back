package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;

import java.util.List;

public interface BadgeService {
    BadgeDTO addBadge(BadgeDTO badgeDTO, Long userId);
    List<BadgeDTO> getBadges();
    BadgeDTO getBadgeById(Long id);
    List<BadgeDTO> getUserBadges(Long userId);
    BadgeDTO updateUserBadgeStatus(Long badgeId, StatusEnum status);
    void checkAndUpdateBadgeStatus(Long userId);
}
