package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.UserChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallengeEntity, Long> {
    List<UserChallengeEntity> findByUserId(Long userId);
}
