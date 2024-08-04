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

    //뱃지 생성
    @Override
    public BadgeDTO addBadge(BadgeDTO badgeDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저의 ID가 " + userId + "인 사용자를 찾을 수 없습니다"));
        BadgeEntity badgeEntity = badgeRepository.save(badgeDTO.dtoToEntity());
        badgeEntity.setUser(user);
        badgeEntity = badgeRepository.save(badgeEntity);  // User 설정 후 다시 저장
        log.info("뱃지가 생성되었습니다! 뱃지 제목 : {}", badgeDTO.getTitle());
        return BadgeDTO.entityToDto(badgeEntity);
    }

    //뱃지 전체 조회
    @Override
    public List<BadgeDTO> getBadges() {
        List<BadgeDTO> badges = badgeRepository.findAll().stream()
                .map(BadgeDTO::entityToDto)
                .collect(Collectors.toList());
        if (badges.isEmpty()) {
            log.info("뱃지가 없습니다.");
        } else {
            log.info("뱃지 조회 완료!");
        }
        return badges;
    }

    //해당 뱃지 조회
    @Override
    public BadgeDTO getBadgeById(Long id) {
        BadgeEntity badgeEntity = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("뱃지의 ID가 " + id + "인 뱃지를 찾을 수 없습니다"));
        log.info("뱃지가 조회되었습니다! 뱃지 ID : {}", id);
        return BadgeDTO.entityToDto(badgeEntity);
    }

    //해당 유저 전체 뱃지 조회
    @Override
    public List<BadgeDTO> getUserBadges(Long userId) {
        List<BadgeDTO> userBadges = badgeRepository.findByUserId(userId).stream()
                .map(BadgeDTO::entityToDto)
                .collect(Collectors.toList());
        if (userBadges.isEmpty()) {
            log.info("유저 ID가 {}의 뱃지가 없습니다.", userId);
        } else {
            log.info("유저 ID가 {}의 뱃지 조회 완료!", userId);
        }
        return userBadges;
    }

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
