package com.example.MangoWafflee.Global.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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

    private final KakaoOAuthProperties kakaoOAuthProperties;

    public CustomOAuth2LoginSuccessHandler(KakaoOAuthProperties kakaoOAuthProperties) {
        this.kakaoOAuthProperties = kakaoOAuthProperties;
    }

    @PostConstruct
    public void logKakaoOAuthSettings() {
        logger.info("Kakao OAuth 설정 값 - clientId : {}, clientSecret : {}, redirectUri : {}", kakaoOAuthProperties.getClientId(), kakaoOAuthProperties.getClientSecret(), kakaoOAuthProperties.getRedirectUri());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("인증 성공!");
        logger.info("인증 세부 정보 : {}", authentication.getDetails());
        logger.info("주요 내용 : {}", authentication.getPrincipal());

        String code = request.getParameter("code");
        logger.info("인가 코드 : {}", code);
        logger.info("리다이렉트 URI : {}", kakaoOAuthProperties.getRedirectUri());

        if (code != null) {
            String tokenEndpoint = "https://kauth.kakao.com/oauth/token";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoOAuthProperties.getClientId());
            params.add("client_secret", kakaoOAuthProperties.getClientSecret());
            params.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
            params.add("code", code);

            logger.info("토큰 요청 파라미터 (위치 : CustomOAuth2LoginSuccessHandler) : {}", params);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, String.class);
                logger.info("액세스 토큰 응답 : {}", responseEntity.getBody());
            } catch (HttpClientErrorException e) {
                logger.error("토큰 발급 중 클라이언트 오류 발생 (위치 : CustomOAuth2LoginSuccessHandler) : 상태 코드 = {}, 응답 본문 = {}", e.getStatusCode(), e.getResponseBodyAsString());
                logger.error("에러 상세 정보 : ", e);
                response.sendRedirect("/loginFailure");
            } catch (Exception e) {
                logger.error("토큰 발급 중 오류 발생 (위치 : CustomOAuth2LoginSuccessHandler)", e);
                response.sendRedirect("/loginFailure");
            }
        } else {
            logger.error("인가 코드가 존재하지 않습니다.");
            response.sendRedirect("/loginFailure");
        }

        response.sendRedirect("/");
    }
}
