package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.DTO.UserBadgeDTO;
import com.example.MangoWafflee.Enum.StatusEnum;

import java.util.List;

public interface BadgeService {
    BadgeDTO addBadge(BadgeDTO badgeDTO);
    List<BadgeDTO> getBadges();
    BadgeDTO getBadgeById(Long id);
    List<UserBadgeDTO> getUserBadges(Long userId);
    UserBadgeDTO updateUserBadgeStatus(Long userBadgeId, StatusEnum status);
    UserBadgeDTO createUserBadge(UserBadgeDTO userBadgeDTO);
}
