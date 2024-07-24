package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    //카카오 로그인 성공 시 호출되는 메서드
    @GetMapping("/loginSuccess")
    public JWTDTO loginSuccess(OAuth2User oAuth2User) {
        return userService.loginWithOAuth2(oAuth2User);
    }
}
