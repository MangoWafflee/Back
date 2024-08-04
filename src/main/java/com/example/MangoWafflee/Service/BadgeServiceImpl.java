package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Repository.BadgeRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    //유저 뱃지 상태 업데이트
    @Override
    public BadgeDTO updateUserBadgeStatus(Long badgeId, StatusEnum status) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new RuntimeException("Badge ID가 " + badgeId + "인 뱃지를 찾을 수 없습니다"));

        badgeEntity.setIsAchieved(status);
        if (status == StatusEnum.성공) {
            badgeEntity.setAchievedAt(LocalDate.now());
        }

        BadgeEntity updatedBadge = badgeRepository.save(badgeEntity);
        log.info("유저 뱃지 ID {}의 상태가 {}로 업데이트 되었습니다.", badgeId, status);
        return BadgeDTO.entityToDto(updatedBadge);
    }

    //뱃지 목표 자동 달성
    @Override
    public void checkAndUpdateBadgeStatus(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저의 ID가 " + userId + "인 사용자를 찾을 수 없습니다"));
        int smileCount = user.getSmilecount();

        List<BadgeEntity> userBadges = badgeRepository.findByUserId(userId);
        for (BadgeEntity badge : userBadges) {
            if (smileCount >= badge.getRequiredSmileCount() && badge.getIsAchieved() != StatusEnum.성공) {
                badge.setIsAchieved(StatusEnum.성공);
                badge.setAchievedAt(LocalDate.now());
                badgeRepository.save(badge);
                log.info("유저 ID {}의 뱃지 ID {}가 성공으로 업데이트 되었습니다.", userId, badge.getId());
            }
        }
    }
}
