package com.example.MangoWafflee.Service.Impl;

import com.example.MangoWafflee.DAO.SmileDAO;
import com.example.MangoWafflee.DTO.SmileDTO;
import com.example.MangoWafflee.Entity.SmileEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Service.BadgeService;
import com.example.MangoWafflee.Service.ChallengeService;
import com.example.MangoWafflee.Service.SmileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmileServiceImpl implements SmileService {

    private final SmileDAO smileDAO;
    private final UserRepository userRepository;
    private final BadgeService badgeService;
    private final ChallengeService challengeService;

    @Autowired
    public SmileServiceImpl(SmileDAO smileDAO, UserRepository userRepository, BadgeService badgeService, ChallengeService challengeService) {
        this.smileDAO = smileDAO;
        this.userRepository = userRepository;
        this.badgeService = badgeService;
        this.challengeService = challengeService;
    }

    @Override
    public SmileDTO saveSmile(SmileDTO smileDTO) throws Exception {
        UserEntity user = userRepository.findByNickname(smileDTO.getNickname());
        if (user != null) {
            SmileEntity smileEntity = SmileDTO.toEntity(smileDTO);
            smileEntity.setUser(user);
            SmileEntity savedSmile = smileDAO.save(smileEntity);

            //유저 smilecount 업데이트 추가
            user.setSmilecount(user.getSmilecount() + 1);
            //업데이트된 유저 정보 저장 추가
            userRepository.save(user);
            //유저 뱃지 상태 업데이트 추가
            badgeService.checkAndUpdateBadgeStatus(user.getId());
            //챌린지 상태 업데이트 추가
            challengeService.checkAndUpdateChallengeStatus(user.getId());

            return SmileDTO.fromEntity(savedSmile);
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public List<SmileDTO> getSmileByNickname(String nickname) throws Exception {
        UserEntity user = userRepository.findByNickname(nickname);
        if (user != null) {
            List<SmileEntity> smileEntities = smileDAO.findByUser(user);
            return smileEntities.stream()
                    .map(SmileDTO::fromEntity)
                    .toList();
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public List<SmileDTO> getSmileByNicknameAndYearAndMonth(String nickname, int year, int month) throws Exception {
        UserEntity user = userRepository.findByNickname(nickname);
        if (user != null) {
            List<SmileEntity> smileEntities = smileDAO.findByNicknameAndYearAndMonth(nickname, year, month);
            return smileEntities.stream()
                    .map(SmileDTO::fromEntity)
                    .toList();
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public List<SmileDTO> getSmileByUserNameAndYearAndMonthAndDay(String nickname, int year, int month, Integer day) throws Exception {
        UserEntity user = userRepository.findByNickname(nickname);
        if (user != null) {
            List<SmileEntity> smileEntities = smileDAO.findByNicknameAndYearAndMonthAndDay(nickname, year, month, day);
            return smileEntities.stream()
                    .map(SmileDTO::fromEntity)
                    .toList();
        } else {
            throw new Exception("User not found");
        }    }

    @Override
    public List<SmileDTO> getAllSmile() {
        List<SmileEntity> smileEntities = smileDAO.findAll();
        return smileEntities.stream()
                .map(SmileDTO::fromEntity)
                .toList();
    }
}
