package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {
    Optional<ChallengeEntity> findByTitle(String title);
}
