package com.example.MangoWafflee.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MangoWafflee.Entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);

    Member findByLoginId(String loginId);
}

