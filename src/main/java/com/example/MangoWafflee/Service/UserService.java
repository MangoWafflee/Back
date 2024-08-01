package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByUid(String uid, UserDetails userDetails);
    UserDTO getUserByNickname(String nickname, UserDetails userDetails);
    String isUidDuplicate(String uid);
    String isNicknameDuplicate(String nickname);
    JWTDTO login(String uid, String password);
    UserDTO updateUser(UserDTO userDTO, MultipartFile image, UserDetails userDetails);
    void deleteUser(String uid, UserDetails userDetails);
    Long refreshToken(UserDetails userDetails);
    Long getTokenRemainingTime(UserDetails userDetails);
    JWTDTO getUserWithTokenInfo(String uid, String token);
    UserDTO updateNickname(String uid, String nickname, UserDetails userDetails);
    String getAccessToken(String code);
    JWTDTO loginWithOAuth2(String code);
    UserDTO getKakaoUserInfo(String uid);
    UserDTO addImageToUser(String uid, MultipartFile image, UserDetails userDetails);
    UserDTO getUserById(Long userId);
}
