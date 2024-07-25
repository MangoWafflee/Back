package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.SmileDTO;

import java.util.List;

public interface SmileService {
    SmileDTO saveSmile(SmileDTO smileDTO) throws Exception;
    List<SmileDTO> getSmileByNickname(String nickname) throws Exception;
    List<SmileDTO> getSmileByNicknameAndYearAndMonth(String nickname, int year, int month) throws Exception;
    List<SmileDTO> getAllSmile();
}
