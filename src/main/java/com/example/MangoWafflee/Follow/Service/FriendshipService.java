package com.example.MangoWafflee.Follow.Service;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.Entity.Friendship;
import com.example.MangoWafflee.Follow.Repository.FriendshipRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getFriends(Long userId, UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("인증된 유저가 아닙니다.");
        }
        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + username + "인 사용자를 찾을 수 없습니다"));

        List<Friendship> friendships = friendshipRepository.findAllByUserId(userId);

        return friendships.stream()
                .map(friendship -> {
                    if (friendship.getUser1().getId().equals(userId)) {
                        return friendship.getUser2();
                    } else {
                        return friendship.getUser1();
                    }
                })
                .collect(Collectors.toList());
    }
}
