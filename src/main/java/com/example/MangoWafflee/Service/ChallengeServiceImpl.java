package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.DTO.UserChallengeDTO;
import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Entity.UserChallengeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Enum.StatusEnum;
import com.example.MangoWafflee.Repository.ChallengeRepository;
import com.example.MangoWafflee.Repository.UserChallengeRepository;
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
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;

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

    //유저 챌린지 참여
    @Override
    public UserChallengeDTO participateInChallenge(Long userId, Long challengeId, StatusEnum status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
        ChallengeEntity challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지 ID가 " + challengeId + "인 챌린지를 찾을 수 없습니다."));

        UserChallengeEntity userChallenge = UserChallengeEntity.builder()
                .user(user)
                .challenge(challenge)
                .participating(status)
                .build();

        if (status == StatusEnum.참여) {
            challenge.setCount(challenge.getCount() + 1);
            challengeRepository.save(challenge);
        }

        UserChallengeEntity createdUserChallenge = userChallengeRepository.save(userChallenge);
        log.info("유저 ID {}가 챌린지 ID {}에 참여했습니다.", userId, challengeId);

        return UserChallengeDTO.entityToDto(createdUserChallenge);
    }

    //유저 챌린지 성공 여부
    @Override
    public UserChallengeDTO updateUserChallengeStatus(Long userChallengeId, StatusEnum status) {
        UserChallengeEntity userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new RuntimeException("UserChallenge ID가 " + userChallengeId + "인 챌린지를 찾을 수 없습니다."));

        userChallenge.setSuccessStatus(status);

        if (status == StatusEnum.성공) {
            ChallengeEntity challenge = userChallenge.getChallenge();
            challenge.setCompletedAttempts(challenge.getCompletedAttempts() + 1);
            challengeRepository.save(challenge);
        }

        UserChallengeEntity updatedUserChallenge = userChallengeRepository.save(userChallenge);
        log.info("유저 챌린지 ID: {}의 상태가 {}로 업데이트되었습니다.", userChallengeId, status);

        return UserChallengeDTO.entityToDto(updatedUserChallenge);
    }
}
