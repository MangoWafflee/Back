package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.ChallengeRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private static final Logger logger = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChallengeDTO> getChallengesByUserId(Long userId) {
        List<ChallengeDTO> challenges = challengeRepository.findByUserId(userId).stream()
                .map(ChallengeDTO::entityToDto)
                .collect(Collectors.toList());
        if (challenges.isEmpty()) {
            logger.info("챌린지가 없습니다.");
        } else {
            logger.info("챌린지 조회 완료! 사용자 ID : {}", userId);
        }
        return challenges;
    }

    @Override
    public ChallengeDTO addChallenge(ChallengeDTO challengeDTO) {
        UserEntity user = userRepository.findById(challengeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저의 ID가 " + challengeDTO.getUserId() + "인 사용자를 찾을 수 없습니다"));
        ChallengeEntity challengeEntity = challengeRepository.save(challengeDTO.dtoToEntity(user));
        logger.info("챌린지가 생성되었습니다! 챌린지 제목 : {}, 사용자 ID : {}", challengeDTO.getTitle(), challengeDTO.getUserId());
        return ChallengeDTO.entityToDto(challengeEntity);
    }
}
