package com.example.MangoWafflee.Service.Impl;

import com.example.MangoWafflee.DAO.SmileDAO;
import com.example.MangoWafflee.DTO.SmileDTO;
import com.example.MangoWafflee.Entity.SmileEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Service.SmileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmileServiceImpl implements SmileService {

    private final SmileDAO smileDAO;
    private final UserRepository userRepository;

    @Autowired
    public SmileServiceImpl(SmileDAO smileDAO, UserRepository userRepository) {
        this.smileDAO = smileDAO;
        this.userRepository = userRepository;
    }

    @Override
    public SmileDTO saveSmile(SmileDTO smileDTO) throws Exception {
        UserEntity user = userRepository.findByNickname(smileDTO.getNickname());
        if (user != null) {
            SmileEntity smileEntity = SmileDTO.toEntity(smileDTO);
            smileEntity.setUser(user);
            SmileEntity savedSmile = smileDAO.save(smileEntity);
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
