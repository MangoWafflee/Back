package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {
    List<ChallengeEntity> findByUserId(Long userId);
}