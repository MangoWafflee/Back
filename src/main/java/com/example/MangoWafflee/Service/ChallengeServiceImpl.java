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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
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

    //챌린지 데이터
    @Bean
    public CommandLineRunner initChallenges() {
        return args -> {
            List<ChallengeEntity> challenges = List.of(
                    new ChallengeEntity(null, "[8월] 7일 웃기 챌린지", "이번 달 7일 웃어보세요.", "8월에는 챌린지를 통해 7번 웃어봐요. 이번 달에 7일 웃고 챌린지를 성공해보세요!'", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31), StatusEnum.진행중, 0, 0, 0, "https://nongburang-images.s3.ap-northeast-2.amazonaws.com/challenge_24_08_7.png"),
                    new ChallengeEntity(null, "[8월] 14일 웃기 챌린지", "이번 달 14일 웃어보세요.", "8월에는 챌린지를 통해 14번 웃어봐요. 이번 달에 14일 웃고 챌린지를 성공해보세요!'", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31), StatusEnum.진행중, 0, 0, 0, "https://nongburang-images.s3.ap-northeast-2.amazonaws.com/challenge_24_08_14.png"),
                    new ChallengeEntity(null, "[8월] 20일 웃기 챌린지", "이번 달 20일 웃어보세요.", "8월에는 챌린지를 통해 20번 웃어봐요. 이번 달에 20일 웃고 챌린지를 성공해보세요!'", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31), StatusEnum.진행중, 0, 0, 0, "https://nongburang-images.s3.ap-northeast-2.amazonaws.com/challenge_24_08_20.png"),
                    new ChallengeEntity(null, "[7월] 7일 웃기 챌린지", "이번 달 7일 웃어보세요.", "7월에는 챌린지를 통해 7번 웃어봐요. 이번 달에 7일 웃고 챌린지를 성공해보세요!'", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31), StatusEnum.진행완료, 0, 0, 0, "https://nongburang-images.s3.ap-northeast-2.amazonaws.com/challenge_24_07_7.png")
            );
            for (ChallengeEntity challenge : challenges) {
                if (challengeRepository.findByTitle(challenge.getTitle()).isEmpty()) {
                    challengeRepository.save(challenge);
                }
            }
        };
    }

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

    //유저 챌린지 수행 자동 메서드
    private int getChallengeGoal(Long challengeId) {
        switch (challengeId.intValue()) {
            case 1:
                return 7;
            case 2:
                return 14;
            case 3:
                return 20;
            case 4:
                return 7;
            default:
                return Integer.MAX_VALUE;
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

    //해당 챌린지 조회
    @Override
    public ChallengeDTO getChallengeById(Long challengeId) {
        ChallengeEntity challengeEntity = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지 ID가 " + challengeId + "인 챌린지를 찾을 수 없습니다."));
        return ChallengeDTO.entityToDto(challengeEntity);
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
        ChallengeEntity challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지 ID가 " + challengeId + "인 챌린지를 찾을 수 없습니다."));

        //챌린지가 진행완료 상태인 경우 참여 불가
        if (challenge.getStatus() == StatusEnum.진행완료) {
            throw new RuntimeException("이 챌린지는 진행이 완료되어 더이상 참여할 수 없습니다.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        //이미 참여 중인 챌린지가 있는지 확인하는 boolean 값 정의
        boolean isAlreadyParticipating = userChallengeRepository.findByUserId(userId).stream()
                .anyMatch(userChallenge -> userChallenge.getChallenge().getId().equals(challengeId));

        if (isAlreadyParticipating) {
            throw new RuntimeException("이미 참여중인 챌린지입니다.");
        }

        UserChallengeEntity userChallenge = UserChallengeEntity.builder()
                .user(user)
                .challenge(challenge)
                .participating(StatusEnum.참여)
                .build();

        if (status == StatusEnum.참여) {
            challenge.setCount(challenge.getCount() + 1);
            challenge.setTotalAttempts(challenge.getTotalAttempts() + 1);
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

        //해당 유저에 대하여 한 번 성공했으면 CompletedAttempts 필드를 더 이상 증가시키지 않음
        if (userChallenge.getSuccessStatus() != StatusEnum.성공) {
            userChallenge.setSuccessStatus(status);

            if (status == StatusEnum.성공) {
                ChallengeEntity challenge = userChallenge.getChallenge();
                challenge.setCompletedAttempts(challenge.getCompletedAttempts() + 1);
                challengeRepository.save(challenge);
            }
        }

        UserChallengeEntity updatedUserChallenge = userChallengeRepository.save(userChallenge);
        log.info("유저 챌린지 ID: {}의 상태가 {}로 업데이트되었습니다.", userChallengeId, status);

        return UserChallengeDTO.entityToDto(updatedUserChallenge);
    }

    //유저 챌린지 수행 자동 메서드
    @Override
    public void checkAndUpdateChallengeStatus(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        List<UserChallengeEntity> userChallenges = userChallengeRepository.findByUserId(userId);
        for (UserChallengeEntity userChallenge : userChallenges) {
            if (user.getSmilecount() >= getChallengeGoal(userChallenge.getChallenge().getId()) && userChallenge.getSuccessStatus() != StatusEnum.성공) {
                userChallenge.setSuccessStatus(StatusEnum.성공);
                userChallengeRepository.save(userChallenge);
                ChallengeEntity challenge = userChallenge.getChallenge();
                challenge.setCompletedAttempts(challenge.getCompletedAttempts() + 1);
                challengeRepository.save(challenge);
            }
        }
    }

    //유저 챌린지 조회
    @Override
    public List<UserChallengeDTO> getUserChallenges(Long userId) {
        return userChallengeRepository.findByUserId(userId).stream()
                .map(UserChallengeDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //진행중인 챌린지만 조회 (상황에 따라 해당 메서드에 Enum값 변경하거나 추가할 것)
    @Override
    public List<ChallengeDTO> getOngoingChallenges() {
        return challengeRepository.findAll().stream()
                .filter(challenge -> challenge.getStatus() == StatusEnum.진행중)
                .map(ChallengeDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //진행완료인 챌린지만 조회
    @Override
    public List<ChallengeDTO> getCompletedChallenges() {
        return challengeRepository.findAll().stream()
                .filter(challenge -> challenge.getStatus() == StatusEnum.진행완료)
                .map(ChallengeDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //대기중인 챌린지만 조회
    @Override
    public List<ChallengeDTO> getPendingChallenges() {
        return challengeRepository.findAll().stream()
                .filter(challenge -> challenge.getStatus() == StatusEnum.대기중)
                .map(ChallengeDTO::entityToDto)
                .collect(Collectors.toList());
    }
}
