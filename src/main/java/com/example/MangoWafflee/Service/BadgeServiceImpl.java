package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.BadgeDTO;
import com.example.MangoWafflee.Entity.BadgeEntity;
import com.example.MangoWafflee.Repository.BadgeRepository;
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

    //뱃지 생성
    @Override
    public BadgeDTO addBadge(BadgeDTO badgeDTO) {
        BadgeEntity badgeEntity = badgeRepository.save(badgeDTO.dtoToEntity());
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
}
