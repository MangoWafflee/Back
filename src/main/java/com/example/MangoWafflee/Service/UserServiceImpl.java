//package com.example.MangoWafflee.Service;
//
//import com.example.MangoWafflee.DTO.UserDTO;
//import com.example.MangoWafflee.Entity.UserEntity;
//import com.example.MangoWafflee.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDTO handleKakaoLogin(OAuth2User oAuth2User) {
//        String kakaoId = oAuth2User.getAttribute("id").toString();
//        Map<String, Object> properties = oAuth2User.getAttribute("properties");
//        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
//
//        String username = (String) properties.get("nickname");
//        String email = (String) kakaoAccount.get("email");
//
//        UserEntity existingUser = userRepository.findByKakaoId(kakaoId);
//
//        if (existingUser == null) {
//            UserEntity userEntity = UserEntity.builder()
//                    .kakaoId(kakaoId)
//                    .username(username)
//                    .email(email)
//                    .build();
//
//            existingUser = userRepository.save(userEntity);
//        }
//
//        return UserDTO.entityToDTO(existingUser);
//    }
//}
