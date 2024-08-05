package com.example.MangoWafflee.Follow.Service;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.DTO.FollowRequestDTO;
import com.example.MangoWafflee.Follow.DTO.FollowResponseDTO;
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

    //토큰 인증 메서드 하나로 통합
    private void validateAuthenticatedUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("인증된 유저가 아닙니다.");
        }
    }

    public FollowRequest sendFollowRequest(Long receiverId, UserDetails userDetails) {
        validateAuthenticatedUser(userDetails);
        String username = userDetails.getUsername();
        UserEntity sender = userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + username + "인 사용자를 찾을 수 없습니다"));
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

    public void respondToRequest(FollowResponseDTO followResponseDTO, UserDetails userDetails) {
        validateAuthenticatedUser(userDetails);
        Long requestId = followResponseDTO.getRequestId();
        FollowRequestStatus status = followResponseDTO.getStatus();

        FollowRequest request = followRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("일치하는 requestId가 없습니다."));

        if (status == null) {
            throw new IllegalArgumentException("Invalid status: status는 null일 수 없습니다.");
        }

        try {
            FollowRequestStatus.valueOf(status.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: status가 REJECTED 또는 ACCEPTED인지 확인하세요.");
        }

        if (status == FollowRequestStatus.REJECTED) {
            followRequestRepository.delete(request);
            throw new RuntimeException("팔로우를 거절합니다. 팔로우 요청을 삭제합니다.");
        } else if (status == FollowRequestStatus.ACCEPTED) {
            createFriendship(request);
            followRequestRepository.delete(request);
            throw new RuntimeException("✨ 팔로우 수락 완료! 우리는 칭긔");
        } else {
            throw new IllegalArgumentException("Invalid status: status는 REJECTED 또는 ACCEPTED 값이어야 합니다.");
        }
    }

    private void createFriendship(FollowRequest request) {
        Friendship friendship = new Friendship();
        friendship.setUser1(request.getSender());
        friendship.setUser2(request.getReceiver());
        friendship.setFriendshipDate(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    public List<FollowRequestDTO> getSentFollowRequests(Long senderId, UserDetails userDetails) {
        validateAuthenticatedUser(userDetails);

        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + username + "인 사용자를 찾을 수 없습니다"));

        List<FollowRequest> followRequests = followRequestRepository.findAllBySenderId(senderId);
        return followRequests.stream()
                .map(FollowRequestDTO::entityToDto)
                .collect(Collectors.toList());
    }

    public List<FollowRequestDTO> getReceivedFollowRequests(Long receiverId, UserDetails userDetails) {
        validateAuthenticatedUser(userDetails);

        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + username + "인 사용자를 찾을 수 없습니다"));

        List<FollowRequest> followRequests = followRequestRepository.findByReceiverId(receiverId);
        return followRequests.stream()
                .map(FollowRequestDTO::entityToDto)
                .collect(Collectors.toList());
    }
}