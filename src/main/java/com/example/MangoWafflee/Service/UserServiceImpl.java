package com.example.MangoWafflee.Service;

import com.example.MangoWafflee.DTO.JWTDTO;
import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Global.Config.JWT.JwtTokenProvider;
import com.example.MangoWafflee.Global.Config.KakaoOAuthProperties;
import com.google.api.client.util.Value;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Storage;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;

import java.io.IOException;
import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Storage storage;
    private final RestTemplate restTemplate;
    private final KakaoOAuthProperties kakaoOAuthProperties;

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

    //uid로 회원 조회
    @Override
    public UserDTO getUserByUid(String uid, UserDetails userDetails) {
        if (!userDetails.getUsername().equals(uid)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));
        return UserDTO.entityToDto(userEntity);
    }

    //닉네임으로 유저 조회
    @Override
    public UserDTO getUserByNickname(String nickname, UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByNickname(nickname);
        if (userEntity == null) {
            throw new RuntimeException("유저의 nickname이 " + nickname + "인 사용자를 찾을 수 없습니다");
        }
        if (!userEntity.getUid().equals(userDetails.getUsername())) {
            throw new RuntimeException("권한이 없습니다.");
        }
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
    public UserDTO updateUser(UserDTO userDTO, MultipartFile image, UserDetails userDetails) {
        String uid = userDTO.getUid();
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
        if (image != null && !image.isEmpty()) {
            try {
                UUID uuid = UUID.randomUUID();
                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String fileName = uuid.toString() + fileExtension;
                String contentType;
                switch (fileExtension.toLowerCase()) {
                    case ".jpg":
                    case ".jpeg":
                        contentType = "image/jpeg";
                        break;
                    case ".png":
                        contentType = "image/png";
                        break;
                    case ".bmp":
                        contentType = "image/bmp";
                        break;
                    default:
                        contentType = "application/octet-stream";
                }
                BlobId blobId = BlobId.of("mangowafflee", fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(contentType)
                        .setContentDisposition("inline; filename=" + image.getOriginalFilename())
                        .build();
                storage.create(blobInfo, image.getBytes());
                String imageUrl = "https://storage.cloud.google.com/mangowafflee/" + fileName;
                userEntity.setImage(imageUrl);
                logger.info("사용자 프로필 이미지 업데이트 완료! " + userEntity);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
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

    //유저 토큰 정보 조회
    @Override
    public JWTDTO getUserWithTokenInfo(String uid, String token) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));

        Long remainingTime = jwtTokenProvider.getTokenRemainingTime(token);
        return new JWTDTO(token, UserDTO.entityToDto(userEntity), remainingTime);
    }

    @PostConstruct
    public void logKakaoOAuthSettings() {
        logger.info("Kakao OAuth 설정 값 - clientId : {}, clientSecret : {}, redirectUri : {}", kakaoOAuthProperties.getClientId(), kakaoOAuthProperties.getClientSecret(), kakaoOAuthProperties.getRedirectUri());
    }

    //카카오 인가 코드로 액세스 토큰 요청
    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthProperties.getClientId());
        params.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
        params.add("code", code);
        params.add("client_secret", kakaoOAuthProperties.getClientSecret());

        logger.info("액세스 토큰 요청 URL: {}", url);
        logger.info("액세스 토큰 요청 헤더: {}", headers);
        logger.info("액세스 토큰 요청 파라미터: {}", params);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                String accessToken = (String) responseBody.get("access_token");
                logger.info("액세스 토큰을 성공적으로 가져왔습니다: {}", accessToken);
                return accessToken;
            } else {
                logger.error("액세스 토큰을 가져오는데 실패했습니다. 응답 본문이 비어있습니다.");
                return null;
            }
        } catch (HttpClientErrorException e) {
            logger.error("액세스 토큰을 가져오는 중 오류가 발생하였습니다. (위치: getAccessToken): {}", e.getMessage());
            logger.error("응답 본문 (위치: getAccessToken): {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    //액세스 토큰으로 사용자 정보 요청
    public Map<String, Object> getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                logger.info("사용자 정보를 성공적으로 가져왔습니다 : {}", responseBody);
                return responseBody;
            } else {
                logger.error("사용자 정보를 가져오는데 실패했습니다. 응답 본문이 비어있습니다.");
                return null;
            }
        } catch (HttpClientErrorException e) {
            logger.error("사용자 정보를 가져오는 중 오류가 발생했습니다. (위치: getUserInfo): {}", e.getMessage());
            logger.error("응답 본문 (위치: getUserInfo): {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    //카카오 로그인 처리
    @Override
    public JWTDTO loginWithOAuth2(String code) {
        try {
            String accessToken = getAccessToken(code);
            Map<String, Object> userInfo = getUserInfo(accessToken);

            String uid = String.valueOf(userInfo.get("id"));
            Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

            String name = (String) properties.get("nickname");
            String nickname = (String) kakaoAccount.get("profile_nickname");
            String email = (String) kakaoAccount.get("email");

            UserEntity userEntity = userRepository.findByUid(uid).orElse(null);

            if (userEntity == null) {
                userEntity = UserEntity.builder()
                        .uid(uid)
                        .name(name)
                        .email(email)
                        .nickname(nickname)
                        .password(passwordEncoder.encode("oauth2user"))
                        .provider("kakao")
                        .build();
                userRepository.save(userEntity);
            } else {
                userEntity.setName(name);
                userEntity.setEmail(email);
                userEntity.setNickname(nickname);
                userRepository.save(userEntity);
            }

            String token = jwtTokenProvider.generateToken(uid);
            logger.info("카카오 로그인 성공! 새로운 토큰이 발급되었습니다");
            return new JWTDTO(token, UserDTO.entityToDto(userEntity));
        } catch (Exception e) {
            logger.error("카카오 로그인 중 오류가 발생했습니다 (위치 : loginWithOAuth2) : {}", e.getMessage());
            throw new RuntimeException("카카오 로그인 중 오류가 발생했습니다. (위치 : loginWithOAuth2)", e);
        }
    }

    //카카오 로그인 유저 정보 조회
    @Override
    public UserDTO getKakaoUserInfo(String uid) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));
        return UserDTO.entityToDto(userEntity);
    }

    //카카오 유저 프로필 이미지 설정
    @Override
    public UserDTO addImageToUser(String uid, MultipartFile image, UserDetails userDetails) {
        // 사용자 인증
        if (!userDetails.getUsername().equals(uid)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));

        if (image != null && !image.isEmpty()) {
            try {
                // UUID를 사용해 버킷에 저장되는 미디어 파일들이 파일 이름 중복으로 충돌이 일어나지 않음
                UUID uuid = UUID.randomUUID();
                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String fileName = uuid.toString() + fileExtension;
                String contentType;
                switch (fileExtension.toLowerCase()) {
                    case ".jpg":
                    case ".jpeg":
                        contentType = "image/jpeg";
                        break;
                    case ".png":
                        contentType = "image/png";
                        break;
                    case ".bmp":
                        contentType = "image/bmp";
                        break;
                    default:
                        contentType = "application/octet-stream";
                }
                BlobId blobId = BlobId.of("mangowafflee", fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(contentType)
                        .setContentDisposition("inline; filename=" + image.getOriginalFilename())
                        .build();
                storage.create(blobInfo, image.getBytes());
                String imageUrl = "https://storage.cloud.google.com/mangowafflee/" + fileName;
                userEntity.setImage(imageUrl);
                userRepository.save(userEntity);
                logger.info("사용자 프로필 이미지 업데이트 완료! " + userEntity);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        } else {
            throw new RuntimeException("유효하지 않은 이미지 파일입니다.");
        }
        return UserDTO.entityToDto(userEntity);
    }

    //카카오 유저 닉네임 설정
    @Override
    public UserDTO updateNickname(String uid, String nickname, UserDetails userDetails) {
        // 사용자 인증
        if (!userDetails.getUsername().equals(uid)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("유저의 uid가 " + uid + "인 사용자를 찾을 수 없습니다"));

        userEntity.setNickname(nickname);

        UserEntity updatedUser = userRepository.save(userEntity);
        logger.info("사용자 닉네임 업데이트 완료! " + updatedUser);
        return UserDTO.entityToDto(updatedUser);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저의 id가 " + userId + "인 사용자를 찾을 수 없습니다"));
        return UserDTO.entityToDto(userEntity);
    }
}
