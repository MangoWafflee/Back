package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Date;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByUid(String uid);
    boolean isUidDuplicate(String uid);
    boolean isNicknameDuplicate(String nickname);
    JWTDTO login(String uid, String password);
    UserDTO updateUser(String uid, UserDTO userDTO, UserDetails userDetails);
    void deleteUser(String uid, UserDetails userDetails);
    Long refreshToken(UserDetails userDetails);
    Long getTokenRemainingTime(UserDetails userDetails);
    JWTDTO loginWithOAuth2(OAuth2User oAuth2User);
}
