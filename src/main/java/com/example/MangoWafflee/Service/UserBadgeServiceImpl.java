package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.UserBadgeDTO;
import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserBadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Repository.UserBadgeRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBadgeServiceImpl implements UserBadgeService {
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    //유저 뱃지 생성
    @Override
    public UserBadgeDTO createUserBadge(UserBadgeDTO userBadgeDTO) {
        UserEntity user = userRepository.findById(userBadgeDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("유저의 ID가 " + userBadgeDTO.getUser().getId() + "인 사용자를 찾을 수 없습니다"));
        BadgeEntity badge = badgeRepository.findById(userBadgeDTO.getBadge().getId())
                .orElseThrow(() -> new RuntimeException("뱃지의 ID가 " + userBadgeDTO.getBadge().getId() + "인 뱃지를 찾을 수 없습니다"));

        UserBadgeEntity userBadgeEntity = userBadgeDTO.dtoToEntity(user, badge);
        userBadgeEntity.setIsAchieved(StatusEnum.진행중);

        UserBadgeEntity createdUserBadge = userBadgeRepository.save(userBadgeEntity);
        log.info("유저 ID {}의 뱃지 ID {}가 생성되었습니다.", user.getId(), badge.getId());
        return UserBadgeDTO.entityToDto(createdUserBadge);
    }

    //해당 유저 전체 뱃지 조회
    @Override
    public List<UserBadgeDTO> getUserBadges(Long userId) {
        List<UserBadgeDTO> userBadges = userBadgeRepository.findByUserId(userId).stream()
                .map(UserBadgeDTO::entityToDto)
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
    public UserBadgeDTO updateUserBadgeStatus(Long userBadgeId, StatusEnum status) {
        UserBadgeEntity userBadgeEntity = userBadgeRepository.findById(userBadgeId)
                .orElseThrow(() -> new RuntimeException("UserBadge ID가 " + userBadgeId + "인 뱃지를 찾을 수 없습니다"));

        userBadgeEntity.setIsAchieved(status);
        if (status == StatusEnum.성공) {
            userBadgeEntity.setAchievedAt(LocalDate.now());
        }

        UserBadgeEntity updatedUserBadge = userBadgeRepository.save(userBadgeEntity);
        log.info("유저 뱃지 ID {}의 상태가 {}로 업데이트 되었습니다.", userBadgeId, status);
        return UserBadgeDTO.entityToDto(updatedUserBadge);
    }
}
