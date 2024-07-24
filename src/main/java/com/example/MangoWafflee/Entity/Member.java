package com.example.MangoWafflee.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private String loginId;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // 소셜 미디어 속성
    private String provider;

    // 각 소셜 미디어에 로그인 한 유저의 고유 ID 추출
    private String providerId;
}

