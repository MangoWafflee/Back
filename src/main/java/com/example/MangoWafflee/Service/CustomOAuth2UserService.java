package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String uid = oAuth2User.getAttribute(userNameAttributeName);
        Map<String, Object> properties = oAuth2User.getAttribute("properties");
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");

        String nickname = properties != null ? (String) properties.get("nickname") : null;
        String profileImageUrl = properties != null ? (String) properties.get("profile_image") : null;
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

        Optional<UserEntity> userEntityOptional = userRepository.findByUid(uid);
        UserEntity userEntity;
        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
            userEntity.setNickname(nickname);
            userEntity.setImage(profileImageUrl);
        } else {
            userEntity = UserEntity.builder()
                    .uid(uid)
                    .nickname(nickname)
                    .image(profileImageUrl)
                    .password(passwordEncoder.encode("OAuth2_User_Password"))
                    .build();
            userRepository.save(userEntity);
        }

        return new CustomOAuth2User(userEntity, oAuth2User.getAttributes());
    }
}


