package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.ChallengeDTO;
import com.example.MangoWafflee.Entity.ChallengeEntity;
import com.example.MangoWafflee.Repository.ChallengeRepository;
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

    //챌린지 생성
    @Override
    public ChallengeDTO addChallenge(ChallengeDTO challengeDTO) {
        ChallengeEntity challengeEntity = challengeRepository.save(challengeDTO.dtoToEntity());
        logger.info("챌린지가 생성되었습니다! 챌린지 제목 : {}", challengeDTO.getTitle());
        return ChallengeDTO.entityToDto(challengeEntity);
    }

    //챌린지 전체 조회
    @Override
    public List<ChallengeDTO> getChallenges() {
        List<ChallengeDTO> challenges = challengeRepository.findAll().stream()
                .map(ChallengeDTO::entityToDto)
                .collect(Collectors.toList());
        if (challenges.isEmpty()) {
            logger.info("챌린지가 없습니다.");
        } else {
            logger.info("챌린지 조회 완료!");
        }
        return challenges;
    }

    //해당 챌린지 조회
    @Override
    public ChallengeDTO getChallengeById(Long id) {
        ChallengeEntity challengeEntity = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("챌린지의 ID가 " + id + "인 챌린지를 찾을 수 없습니다"));
        logger.info("챌린지가 조회되었습니다! 챌린지 ID : {}", id);
        return ChallengeDTO.entityToDto(challengeEntity);
    }
}
