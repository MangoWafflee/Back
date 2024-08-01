package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.BadgeRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    @Override
    public List<BadgeDTO> getBadgesByUserId(Long userId) {
        List<BadgeDTO> badges = badgeRepository.findByUserId(userId).stream()
                .map(BadgeDTO::entityToDto)
                .collect(Collectors.toList());
        if (badges.isEmpty()) {
            log.info("사용자의 뱃지가 없습니다. 사용자 ID : {}", userId);
        } else {
            log.info("뱃지 조회 완료! 사용자 ID : {}", userId);
        }
        return badges;
    }

    @Override
    public BadgeDTO addBadge(BadgeDTO badgeDTO) {
        UserEntity user = userRepository.findById(badgeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저의 ID가 " + badgeDTO.getUserId() + "인 사용자를 찾을 수 없습니다"));
        BadgeEntity badgeEntity = badgeRepository.save(badgeDTO.dtoToEntity(user));
        log.info("뱃지가 생성되었습니다! 뱃지 제목 : {}, 사용자 ID : {}", badgeDTO.getTitle(), badgeDTO.getUserId());
        return BadgeDTO.entityToDto(badgeEntity);
    }
}
