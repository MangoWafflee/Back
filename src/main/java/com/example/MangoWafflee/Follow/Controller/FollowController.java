package com.example.MangoWafflee.Follow.Controller;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.DTO.FollowRequestDTO;
import com.example.MangoWafflee.Follow.DTO.FollowResponseDTO;
import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Repository.FollowRequestRepository;
import com.example.MangoWafflee.Follow.Service.FollowService;
import com.example.MangoWafflee.Follow.Service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @PostMapping("/request")
    public ResponseEntity<?> sendFollowRequest(@RequestBody FollowRequestDTO followRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            //DTO에서 수신인 정보를 추출하고 인증된 사용자 정보를 추출
            FollowRequest request = followService.sendFollowRequest(followRequestDTO.getReceiverId(), userDetails);
            //생성된 엔티티를 DTO 클래스 entityToDto 메서드로 DTO 변환
            FollowRequestDTO responseDTO = FollowRequestDTO.entityToDto(request);
            //변환된 DTO 값 반환
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/received")
    public List<FollowRequest> getReceivedFollowRequests(@RequestParam Long userId) {
        return followRequestRepository.findByReceiverId(userId);
    }

    @PostMapping("/response")
    public ResponseEntity<?> respondToRequest(@RequestBody FollowResponseDTO followResponseDTO) {
        return followService.respondToRequest(followResponseDTO.getRequestId(), followResponseDTO.getStatus());
    }

    @GetMapping("/findRequestId/{senderId}")
    public ResponseEntity<?> findRequestId(@PathVariable Long senderId) {
        try {
            List<FollowRequest> sentRequests = followService.getSentFollowRequests(senderId);

            if (sentRequests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 추가 요청 내역을 찾을 수 없습니다.");
            }

            if (sentRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("친구 추가 요청 내역이 비어있습니다.");
            }

            return ResponseEntity.ok(sentRequests);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
        try {
            List<UserEntity> friends = friendshipService.getFriends(userId);

            if (friends.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("친구 목록이 비어있습니다.");
            }
            return ResponseEntity.ok(friends);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
