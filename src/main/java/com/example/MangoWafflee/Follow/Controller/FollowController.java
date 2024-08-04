package com.example.MangoWafflee.Follow.Controller;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.DTO.FollowRequestDTO;
import com.example.MangoWafflee.Follow.DTO.FollowResponseDTO;
import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Repository.FollowRequestRepository;
import com.example.MangoWafflee.Follow.Service.FollowService;
import com.example.MangoWafflee.Follow.Service.FriendshipService;
import com.example.MangoWafflee.Global.Config.JWT.JwtTokenProvider;
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

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/request")
    public ResponseEntity<?> sendFollowRequest(@RequestHeader("Authorization") String token, @RequestBody FollowRequestDTO followRequestDTO) {
        try {
            String jwtToken = token.replace("Bearer ", "");

            // 토큰에서 사용자 ID 추출 및 검증
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            // 토큰에서 발신자의 ID 추출
            String userIdFromToken = jwtTokenProvider.getUidFromToken(jwtToken);

            // 발신자의 토큰과 요청된 발신자 ID가 일치하는지 확인
            if (!userIdFromToken.equals(followRequestDTO.getSenderId().toString())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("SenderId does not match the token's user ID.");
            }

            // 팔로우 요청 처리
            FollowRequest request = followService.sendFollowRequest(followRequestDTO.getSenderId(), followRequestDTO.getReceiverId());
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getReceivedFollowRequests(@RequestHeader("Authorization") String token, @RequestParam Long userId) {
        try {
            String jwtToken = token.replace("Bearer ", "");

            // 토큰 검증
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String userIdFromToken = jwtTokenProvider.getUidFromToken(jwtToken);

            if (!userIdFromToken.equals(String.valueOf(userId))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("userId와 토큰이 일치하지 않습니다.");
            }

            List<FollowRequest> receivedRequests = followRequestRepository.findByReceiverId(userId);
            return ResponseEntity.ok(receivedRequests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/received")
//    public List<FollowRequest> getReceivedFollowRequests(@RequestParam Long userId) {
//        return followRequestRepository.findByReceiverId(userId);
//    }

    @PostMapping("/response")
    public ResponseEntity<?> respondToRequest(@RequestHeader("Authorization") String token, @RequestBody FollowResponseDTO followResponseDTO) {
        try {
            String jwtToken = token.replace("Bearer ", "");

            // 토큰 검증
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            // 토큰에서 사용자의 ID 추출
            String userIdFromToken = jwtTokenProvider.getUidFromToken(jwtToken);

            // 요청에 대한 응답 처리
            return followService.respondToRequest(followResponseDTO.getRequestId(), followResponseDTO.getStatus(), userIdFromToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


//    @PostMapping("/response")
//    public ResponseEntity<?> respondToRequest (@RequestBody FollowResponseDTO followResponseDTO) {
//        return followService.respondToRequest(followResponseDTO.getRequestId(), followResponseDTO.getStatus());
//    }

    @GetMapping("/findRequestId/{senderId}")
    public ResponseEntity<?> findRequestId(@RequestHeader("Authorization") String token, @PathVariable Long senderId) {
        try {
            String jwtToken = token.replace("Bearer ", "");

            // 토큰 검증
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String userIdFromToken = jwtTokenProvider.getUidFromToken(jwtToken);

            if (!userIdFromToken.equals(String.valueOf(senderId))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("senderId와 토큰이 일치하지 않습니다.");
            }

            List<FollowRequest> sentRequests = followService.getSentFollowRequests(senderId);

            if (sentRequests == null || sentRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 추가 요청 내역이 없습니다.");
            }

            return ResponseEntity.ok(sentRequests);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

//    @GetMapping("/findRequestId/{senderId}")
//    public ResponseEntity<?> findRequestId(@PathVariable Long senderId) {
//        try {
//            List<FollowRequest> sentRequests = followService.getSentFollowRequests(senderId);
//
//            if (sentRequests == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 추가 요청 내역을 찾을 수 없습니다.");
//            }
//
//            if (sentRequests.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.OK).body("친구 추가 요청 내역이 비어있습니다.");
//            }
//
//            return ResponseEntity.ok(sentRequests);
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//        }
//    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getFriends(@RequestHeader("Authorization") String token, @PathVariable Long userId) {
        try {
            String jwtToken = token.replace("Bearer ", "");

            // 토큰 검증
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String userIdFromToken = jwtTokenProvider.getUidFromToken(jwtToken);

            if (!userIdFromToken.equals(String.valueOf(userId))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("userId와 토큰이 일치하지 않습니다.");
            }

            List<UserEntity> friends = friendshipService.getFriends(userId);

            if (friends.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("친구 목록이 비어있습니다.");
            }
            return ResponseEntity.ok(friends);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/list/{userId}")
//    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
//        try {
//            List<UserEntity> friends = friendshipService.getFriends(userId);
//
//            if (friends.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.OK).body("친구 목록이 비어있습니다.");
//            }
//            return ResponseEntity.ok(friends);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
}
