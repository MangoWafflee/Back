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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{nickname}")
    public ResponseEntity<List<SmileDTO>> getSmileByNickname(@PathVariable String nickname) {
        try {
            List<SmileDTO> smileDTOs = smileService.getSmileByNickname(nickname);
            return ResponseEntity.ok(smileDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{nickname}/date")
    public ResponseEntity<List<SmileDTO>> getSmileByNicknameAndDate(@PathVariable String nickname, @RequestParam int year, @RequestParam int month) {
        try {
            List<SmileDTO> smileDTOs = smileService.getSmileByNicknameAndYearAndMonth(nickname, year, month);
            return ResponseEntity.ok(smileDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SmileDTO>> getAllSmile() {
        List<SmileDTO> smileDTOs = smileService.getAllSmile();
        return ResponseEntity.ok(smileDTOs);
    }
}
