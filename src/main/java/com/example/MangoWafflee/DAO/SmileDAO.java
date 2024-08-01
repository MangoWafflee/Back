package com.example.MangoWafflee.DAO;

import com.example.MangoWafflee.Entity.SmileEntity;
import com.example.MangoWafflee.Entity.UserEntity;

import java.util.List;

public interface SmileDAO {
    SmileEntity save(SmileEntity smile);
    List<SmileEntity> findByUser(UserEntity user);
    List<SmileEntity> findByNicknameAndYearAndMonth(String nickname, int year, int month);
    List<SmileEntity> findByNicknameAndYearAndMonthAndDay(String nickname, int year, int month, Integer day);
    List<SmileEntity> findAll();
}