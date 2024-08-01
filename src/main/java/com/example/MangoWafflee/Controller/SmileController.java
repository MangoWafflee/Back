package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.SmileDTO;
import com.example.MangoWafflee.Service.SmileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/smile")
public class SmileController {

    private final SmileService smileService;

    @Autowired
    public SmileController(SmileService smileService) {
        this.smileService = smileService;
    }

    @PostMapping("/save")
    public ResponseEntity<SmileDTO> saveSmile(@RequestBody SmileDTO smileDTO) {
        try {
            SmileDTO savedSmile = smileService.saveSmile(smileDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSmile);
        } catch (Exception e) {
            // 로그 추가
            System.err.println("Error saving smile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{nickname}")
    public ResponseEntity<List<SmileDTO>> getSmileByNickname(@PathVariable String nickname) {
        try {
            List<SmileDTO> smileDTOs = smileService.getSmileByNickname(nickname);
            return ResponseEntity.ok(smileDTOs);
        } catch (Exception e) {
            // 로그 추가
            System.err.println("Error fetching smiles for user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{nickname}/date")
    public ResponseEntity<List<SmileDTO>> getSmileByNicknameAndDate(
            @PathVariable String nickname,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Integer day
    ) {
        try {
            List<SmileDTO> smileDTOs;
            if (day != null) {
                smileDTOs = smileService.getSmileByUserNameAndYearAndMonthAndDay(nickname, year, month, day);
            } else {
                smileDTOs = smileService.getSmileByNicknameAndYearAndMonth(nickname, year, month);
            }
            return ResponseEntity.ok(smileDTOs);
        } catch (Exception e) {
            // 로그 추가
            System.err.println("Error fetching smiles for user by date: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SmileDTO>> getAllSmile() {
        try {
            List<SmileDTO> smileDTOs = smileService.getAllSmile();
            return ResponseEntity.ok(smileDTOs);
        } catch (Exception e) {
            // 로그 추가
            System.err.println("Error fetching all smiles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
