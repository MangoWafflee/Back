package com.example.MangoWafflee.Follow.Service;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.Entity.Friendship;
import com.example.MangoWafflee.Follow.Repository.FriendshipRepository;
import com.example.MangoWafflee.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getFriends(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("등록된 유저가 아닙니다.");
        }
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
