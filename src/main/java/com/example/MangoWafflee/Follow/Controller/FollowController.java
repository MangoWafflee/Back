package com.example.MangoWafflee.Follow.Controller;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.DTO.FollowRequestDTO;
import com.example.MangoWafflee.Follow.DTO.FollowResponseDTO;
import com.example.MangoWafflee.Follow.DTO.SenderDTO;
import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/request")
    public ResponseEntity<?> sendFollowRequest(@RequestBody FollowRequestDTO followRequestDTO) {
        try {
            FollowRequest request = followService.sendFollowRequest(followRequestDTO.getSenderId(), followRequestDTO.getReceiverId());
            return ResponseEntity.ok(request);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/response")
    public ResponseEntity<?> respondToRequest(@RequestBody FollowResponseDTO followResponseDTO) {
        return followService.respondToRequest(followResponseDTO.getRequestId(), followResponseDTO.getStatus());
    }

    @PostMapping("/sent")
    public ResponseEntity<?> getSentFollowRequests(@RequestBody SenderDTO senderDTO) {
        Long senderId = senderDTO.getSenderId();
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("senderId 는 null 이 될 수 없습니다.");
        }

        List<FollowRequest> sentRequests = followService.getSentFollowRequests(senderId);

        if (sentRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("친구 추가 요청 내역이 텅 비어있습니다.");
        }

        return ResponseEntity.ok(sentRequests);
    }
}
