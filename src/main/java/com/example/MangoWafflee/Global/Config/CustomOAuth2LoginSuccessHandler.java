package com.example.MangoWafflee.Global.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2LoginSuccessHandler.class);

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 디버깅을 위한 추가 정보 로그
        logger.info("인증 성공!");
        logger.info("인증 세부 정보 : {}", authentication.getDetails());
        logger.info("주요 내용 : {}", authentication.getPrincipal());

        // 인가 코드 로깅
        String code = request.getParameter("code");
        logger.info("인가 코드 : {}", code);

        // 토큰 발급 요청 로직 호출
        if (code != null) {
            String tokenEndpoint = "https://kauth.kakao.com/oauth/token";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, String.class);
                logger.info("액세스 토큰 응답 : {}", responseEntity.getBody());

                // 토큰 발급 성공 후 처리 로직 (예: 토큰 저장 또는 세션 설정)
            } catch (Exception e) {
                logger.error("토큰 발급 중 오류 발생 : {}", e.getMessage());
            }
        }

        // 기본 성공 URL로 리다이렉트
        response.sendRedirect("/");
    }
}
