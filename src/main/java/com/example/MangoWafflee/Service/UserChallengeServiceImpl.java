package com.example.MangoWafflee.Service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserChallengeServiceImpl implements UserChallengeService {
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

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
