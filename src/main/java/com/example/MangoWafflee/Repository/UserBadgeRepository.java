package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.UserBadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadgeEntity, Long> {
    List<UserBadgeEntity> findByUserId(Long userId);
    List<UserBadgeEntity> findByBadgeId(Long badgeId);
}
