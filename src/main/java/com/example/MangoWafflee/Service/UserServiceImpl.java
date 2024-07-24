package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Global.Config.JWT.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (isUidDuplicate(userDTO.getUid())) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다");
        } else if (isNicknameDuplicate(userDTO.getNickname())) {
            throw new IllegalArgumentException("닉네임이 이미 존재합니다");
        }
        UserEntity userEntity = userDTO.dtoToEntity();
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        logger.info("회원가입 완료! " + userEntity);
        return UserDTO.entityToDto(savedUser);
    }

    //회원 조회
    @Override
    public UserDTO getUserByUid(String uid) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));
        return UserDTO.entityToDto(userEntity);
    }

    //아이디 중복 확인
    @Override
    public boolean isUidDuplicate(String uid) {
        return userRepository.existsByUid(uid);
    }

    //닉네임 중복 확인
    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //로그인
    public JWTDTO login(String uid, String password) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        String token = jwtTokenProvider.generateToken(uid);
        logger.info("로그인 성공! 새로운 토큰이 발급되었습니다");
        return new JWTDTO(token, UserDTO.entityToDto(userEntity));
    }

    //회원 정보 수정
    @Override
    public UserDTO updateUser(String uid, UserDTO userDTO, UserDetails userDetails) {
        if (!userDetails.getUsername().equals(uid)) {
            throw new RuntimeException("권한이 없습니다");
        }

        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));

        if (userDTO.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getNickname() != null) {
            userEntity.setNickname(userDTO.getNickname());
        }
        if (userDTO.getImage() != null) {
            userEntity.setImage(userDTO.getImage());
        }

        UserEntity updatedUser = userRepository.save(userEntity);
        logger.info("사용자 정보 업데이트 완료! " + updatedUser);
        return UserDTO.entityToDto(updatedUser);
    }

    //회원 탈퇴
    @Override
    public void deleteUser(String uid, UserDetails userDetails) {
        if (!userDetails.getUsername().equals(uid)) {
            throw new RuntimeException("권한이 없습니다");
        }
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));

        userRepository.delete(userEntity);
        logger.info("유저의 uid가 " + uid + "인 회원탈퇴 완료!");
    }

    //토큰 유효 시간 확인
    @Override
    public Long getTokenRemainingTime(UserDetails userDetails) {
        String uid = userDetails.getUsername();
        String token = jwtTokenProvider.getActiveToken(uid); // 활성화된 토큰을 가져옵니다.
        if (token == null || jwtTokenProvider.isTokenInvalid(token)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다");
        }
        return jwtTokenProvider.getTokenRemainingTime(token);
    }

    //토큰 연장(오류나는 중)
    @Override
    public Long refreshToken(UserDetails userDetails) {
        String uid = userDetails.getUsername();
        String token = jwtTokenProvider.getActiveToken(uid);
        if (token == null) {
            throw new RuntimeException("활성화된 토큰이 없습니다.");
        }
        jwtTokenProvider.refreshToken(token);
        return jwtTokenProvider.getTokenRemainingTime(token);
    }

    //카카오 로그인 성공 시 호출되는 메서드
    public JWTDTO loginWithOAuth2(OAuth2User oAuth2User) {
        String uid = oAuth2User.getName();
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("입력한 사용자 정보에 대해 카카오에 가입된 사용자를 찾을 수 없습니다"));

        String token = jwtTokenProvider.generateToken(uid);
        logger.info("카카오 로그인 성공! 새로운 토큰이 발급되었습니다");
        return new JWTDTO(token, UserDTO.entityToDto(userEntity));
    }
}

