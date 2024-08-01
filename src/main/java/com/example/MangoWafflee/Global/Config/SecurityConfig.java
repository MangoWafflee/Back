package com.example.MangoWafflee.Global.Config;

import com.example.MangoWafflee.Service.CustomOAuth2UserService;
import com.example.MangoWafflee.Repository.UserRepository;
import com.example.MangoWafflee.Global.Config.JWT.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserRepository userRepository;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserRepository userRepository, CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler, CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userRepository = userRepository;
        this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;
        this.customOAuth2LoginFailureHandler = customOAuth2LoginFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login**", "/oauth2/**", "/login", "/loginFailure", "/error", "/user/login").permitAll()  // 일반 로그인 허용
                                .requestMatchers("/user/kakao/**").authenticated()  // 카카오 유저 정보 조회 경로 보호
                                .anyRequest().permitAll() // 모든 요청 허용
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .failureHandler(customOAuth2LoginFailureHandler)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService())
                                )
                                .successHandler(customOAuth2LoginSuccessHandler)
                )
                .formLogin(formLogin -> formLogin.disable());  // 폼 로그인 비활성화

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository, passwordEncoder());
    }
}
