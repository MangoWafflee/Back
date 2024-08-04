package com.example.MangoWafflee.Entity;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String password;
    private String name;
    private String nickname;
    private String image;
    private String email;
    private String provider;
    private int smilecount = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BadgeEntity> badges;

    //동일 유저가 중복해서 무언가를 하지 못하게 할때 엔티티에 생성자를 작성하여 파라미터를 일치시킴 (이렇게 하면 DTO 오류 해결) => 소웨공 참고
    public UserEntity(Long id, String uid, String password, String name, String nickname, String image, String email, String provider, int smilecount) {
        this.id = id;
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.image = image;
        this.email = email;
        this.provider = provider;
        this.smilecount = smilecount;
        this.badges = new ArrayList<>();
    }

    //유저 엔티티와 뱃지 엔티티 간의 양방향 관계를 적절히 설정하기 위해 사용
    public void setBadges(List<BadgeEntity> badges) {
        this.badges = badges;
        if (badges != null) {
            badges.forEach(badge -> badge.setUser(this));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //사용자 권한 설정, 필요에 따라 변경할 것
        return new HashSet<>();
    }

    @Override
    public String getUsername() {
        return uid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
