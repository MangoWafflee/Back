package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody UserDTO userDTO) {
        JWTDTO login = userService.login(userDTO.getUid(), userDTO.getPassword());
        return ResponseEntity.ok(login);
    }

    //유저 조회
    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUserByUid(@PathVariable String uid) {
        UserDTO user = userService.getUserByUid(uid);
        return ResponseEntity.ok(user);
    }

    //아이디 중복 확인
    @GetMapping("/check-uid")
    public ResponseEntity<Boolean> isUidDuplicate(@RequestBody String uid) {
        boolean isDuplicate = userService.isUidDuplicate(uid);
        return ResponseEntity.ok(isDuplicate);
    }

    //닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> isNicknameDuplicate(@RequestBody String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    //회원 정보 수정
    @PutMapping("/{uid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO updatedUser = userService.updateUser(uid, userDTO, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //회원 탈퇴
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(uid, userDetails);
        return ResponseEntity.noContent().build();
    }

    // 토큰 유효 시간 확인
    @GetMapping("/token-remaining-time")
    public ResponseEntity<Long> getTokenRemainingTime(@AuthenticationPrincipal UserDetails userDetails) {
        Long remainingTime = userService.getTokenRemainingTime(userDetails);
        return ResponseEntity.ok(remainingTime);
    }

    //토큰 연장(오류나는 중)
    @PostMapping("/extend-token")
    public ResponseEntity<Long> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        Long remainingTime = userService.refreshToken(userDetails);
        return ResponseEntity.ok(remainingTime);
    }

    //유저 토큰 정보 조회
    @GetMapping("/token/{uid}")
    public ResponseEntity<JWTDTO> getUserWithTokenInfo(@PathVariable String uid, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JWTDTO userWithTokenInfo = userService.getUserWithTokenInfo(uid, token);
        return ResponseEntity.ok(userWithTokenInfo);
    }

    //닉네임 수정
    @PutMapping("/nickname/{uid}")
    public ResponseEntity<UserDTO> updateNickname(@PathVariable String uid, @RequestBody String nickname) {
        UserDTO updatedUser = userService.updateNickname(uid, nickname);
        return ResponseEntity.ok(updatedUser);
    }

    //카카오 로그인 성공 시 호출되는 메서드
    @GetMapping("/loginSuccess")
    public JWTDTO loginSuccess(OAuth2User oAuth2User) {
        return userService.loginWithOAuth2(oAuth2User);
    }

    // 카카오 로그인 유저 정보 조회
    @GetMapping("/kakao/{uid}")
    public ResponseEntity<UserDTO> getKakaoUserInfo(@PathVariable String uid) {
        UserDTO user = userService.getKakaoUserInfo(uid);
        return ResponseEntity.ok(user);
    }

    //카카오 유저 프로필 이미지 등록
    @SneakyThrows
    @PostMapping(value = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTO> addImageToUser(@RequestPart("userData") String userData, @RequestPart("image") MultipartFile image) {
        ObjectMapper mapper = new ObjectMapper();
        UserDTO userDTO = mapper.readValue(userData, UserDTO.class);
        UserDTO updatedUser = userService.addImageToUser(userDTO.getUid(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }
}