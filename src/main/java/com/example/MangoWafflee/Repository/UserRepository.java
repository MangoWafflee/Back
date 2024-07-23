package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByKakaoId(String kakaoId);
}
