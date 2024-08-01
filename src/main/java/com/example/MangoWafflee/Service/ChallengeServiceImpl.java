package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;

    //현재 시간에 따른 챌린지 상태 계산 메서드
    private StatusEnum calculateStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return StatusEnum.대기중;
        } else if (today.isAfter(endDate)) {
            return StatusEnum.진행완료;
        } else {
            return StatusEnum.진행중;
        }
    }

    //챌린지 생성
    @Override
    public ChallengeDTO createChallenge(ChallengeDTO challengeDTO) {
        ChallengeEntity challengeEntity = challengeDTO.dtoToEntity();
        challengeEntity.setCount(0);
        challengeEntity.setTotalAttempts(0);
        challengeEntity.setCompletedAttempts(0);
        challengeEntity.setStatus(calculateStatus(challengeEntity.getStartDate(), challengeEntity.getEndDate()));

        ChallengeEntity createdChallenge = challengeRepository.save(challengeEntity);
        log.info("챌린지가 생성되었습니다. 챌린지 ID : {}", createdChallenge.getId());
        return ChallengeDTO.entityToDto(createdChallenge);
    }

    //챌린지 전체 조회
    @Override
    public List<ChallengeDTO> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(challengeEntity -> {
                    challengeEntity.setStatus(calculateStatus(challengeEntity.getStartDate(), challengeEntity.getEndDate()));
                    return ChallengeDTO.entityToDto(challengeEntity);
                })
                .collect(Collectors.toList());
    }

    //챌린지 상태 업데이트
    @Override
    public ChallengeDTO updateChallengeStatus(Long challengeId, StatusEnum status) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지 ID가 " + challengeId + "인 챌린지를 찾을 수 없습니다."));

        challengeEntity.setStatus(status);

        ChallengeEntity updatedChallenge = challengeRepository.save(challengeEntity);
        log.info("챌린지 ID {}의 상태가 {}로 업데이트되었습니다.", challengeId, status);
        return ChallengeDTO.entityToDto(updatedChallenge);
    }
}
