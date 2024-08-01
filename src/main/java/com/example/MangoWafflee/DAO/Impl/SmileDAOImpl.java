package com.example.MangoWafflee.DAO.Impl;

import com.example.MangoWafflee.DAO.SmileDAO;
import com.example.MangoWafflee.Entity.SmileEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.SmileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SmileDAOImpl implements SmileDAO {

    private final SmileRepository smileRepository;

    @Autowired
    public SmileDAOImpl(SmileRepository smileRepository) {
        this.smileRepository = smileRepository;
    }

    @Override
    public SmileEntity save(SmileEntity smile) {
        return smileRepository.save(smile);
    }

    @Override
    public List<SmileEntity> findByUser(UserEntity user) {
        return smileRepository.findByUser(user);
    }

    @Override
    public List<SmileEntity> findByNicknameAndYearAndMonth(String nickname, int year, int month) {
        return smileRepository.findByNicknameAndYearAndMonth(nickname, year, month);
    }

    @Override
    public List<SmileEntity> findByNicknameAndYearAndMonthAndDay(String nickname, int year, int month, Integer day) {
        return smileRepository.findByNicknameAndYearAndMonthAndDay(nickname, year, month, day);
    }

    @Override
    public List<SmileEntity> findAll() {
        return smileRepository.findAll();
    }
}
