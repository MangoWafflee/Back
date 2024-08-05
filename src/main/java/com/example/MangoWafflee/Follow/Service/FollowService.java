package com.example.MangoWafflee.Follow.Service;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Entity.FollowRequestStatus;
import com.example.MangoWafflee.Follow.Entity.Friendship;
import com.example.MangoWafflee.Follow.Repository.FollowRequestRepository;
import com.example.MangoWafflee.Follow.Repository.FriendshipRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {
    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public FollowRequest sendFollowRequest(Long receiverId, UserDetails userDetails) {
        //UserDetails에서 UserEntity로 끌고옴
        UserEntity sender = (UserEntity) userDetails;
        //인증된 사용자 정보 추출
        Long senderId = sender.getId();

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("자기 자신에게 팔로우 요청을 보낼 수 없습니다.");
        }
        if (isPending(senderId, receiverId)) { // case 1. status == PENDING
            throw new RuntimeException("이미 친구 추가를 요청한 상태입니다.");
        }
        if (isFriend(senderId, receiverId)) { // case 2. 이미 friendship 에 등록되어 있는 경우
            throw new RuntimeException("이 분은 당신의 친구입니다...");
        }

        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        FollowRequest request = new FollowRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FollowRequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());

        return followRequestRepository.save(request);
    }

    // case 1. status == PENDING
    public boolean isPending(Long user1Id, Long user2Id) {
        Optional<FollowRequest> followRequest = followRequestRepository.findPendingRequest(user1Id, user2Id, FollowRequestStatus.PENDING);
        return followRequest.isPresent();
    }

    // case 2. 이미 friendship 에 등록되어 있는 경우
    public boolean isFriend(Long user1Id, Long user2Id) {
        Optional<Friendship> friendship = friendshipRepository.findFriendship(user1Id, user2Id);
        return friendship.isPresent();
    }

    public ResponseEntity<?> respondToRequest(Long requestId, FollowRequestStatus status) { // 팔로우 수락
        Optional<FollowRequest> optionalRequest = followRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) { // case 1. 팔로우 요청 아이디 유효 검증
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 requestId 가 없습니다.");
        }
        FollowRequest request = optionalRequest.get();

        if (status == null) { // case 2. status 유효성 체크
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status: status 는 null 일 수 없습니다.");
        }
        try {
            FollowRequestStatus.valueOf(status.name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status: status 가 REJECTED 또는 ACCEPTED 인지 확인하세요.");
        }

        // 팔로우 요청 거절
        if (status == FollowRequestStatus.REJECTED) {
            followRequestRepository.delete(request);
            return ResponseEntity.status(HttpStatus.OK).body("팔로우를 거절합니다. 팔로우 요청을 삭제합니다.");
        }

        // 팔로우 요청 수락 => 친구 맺기
        if (status == FollowRequestStatus.ACCEPTED) {
            createFriendship(request);
            followRequestRepository.delete(request);
            return ResponseEntity.status(HttpStatus.OK).body("✨ 팔로우 수락 완료! 우리는 칭긔");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Invalid status: status 는 REJECTED 또는 ACCEPTED 값이여야 합니다.");
    }

    private void createFriendship(FollowRequest request) {
        Friendship friendship = new Friendship();
        friendship.setUser1(request.getSender());
        friendship.setUser2(request.getReceiver());
        friendship.setFriendshipDate(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    public List<FollowRequest> getSentFollowRequests(Long senderId) {
        if (!userRepository.existsById(senderId)) {
            throw new IllegalArgumentException("등록된 유저가 아닙니다.");
        }

        return followRequestRepository.findAllBySenderId(senderId);
    }
}