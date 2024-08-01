package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

}
