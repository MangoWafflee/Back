package com.example.MangoWafflee.Global.Config;

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

import com.example.MangoWafflee.Global.Config.JWT.JwtAuthenticationFilter;
import com.example.MangoWafflee.Service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
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
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/loginSuccess")
                                .failureUrl("/loginFailure")
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

//package com.example.MangoWafflee.Config;
//
//        import org.springframework.context.annotation.Bean;
//        import org.springframework.context.annotation.Configuration;
//        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//        import org.springframework.security.web.SecurityFilterChain;
//
//        import lombok.RequiredArgsConstructor;
//        import org.springframework.context.annotation.Bean;
//        import org.springframework.context.annotation.Configuration;
//        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//        import org.springframework.security.web.SecurityFilterChain;
//        import com.example.MangoWafflee.Entity.MemberRole;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
////    private final AuthenticationConfiguration configuration;
////    private final JWTUtil jwtUtil;
//
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
////
////        return configuration.getAuthenticationManager();
////    }
//
//
//    // 시큐리티 필터 메서드
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//
//        // ================== 스프링 시큐리티 로그인 설정 ==================
////        http
////                .authorizeHttpRequests((auth) -> auth
////                        .requestMatchers("/security-login", "/security-login/login", "/security-login/join").permitAll()
////                        .requestMatchers("/security-login/admin").hasRole(MemberRole.ADMIN.name())
////                        .requestMatchers("/security-login/info").hasAnyRole(MemberRole.ADMIN.name(), MemberRole.USER.name())
////                        .anyRequest().authenticated()
////                );
////
////        http
////                .logout((auth) -> auth
////                        .logoutUrl("/security-login/logout")
////                );
////
////        http
////                .formLogin((auth) -> auth.loginPage("/security-login/login")
////                        .loginProcessingUrl("/security-login/loginProc")
////                        // 프론트단에서 설정해 둔 경로로 로그인 정보를 넘기면 스프링 시큐리티가 받아서 자동으로 로그인 진행
////                        .failureUrl("/security-login/login")
////                        .defaultSuccessUrl("/security-login")
////                        .usernameParameter("loginId")
////                        .passwordParameter("password")
////
////                        .permitAll()
////                );
////
////
////        http
////                .csrf((auth) -> auth.disable());
//        // ======================================================
//
//
//        // ================== 스프링 시큐리티 jwt 로그인 설정 ==================
////
////        http
////                .csrf((auth) -> auth.disable());
////        http
////                .formLogin((auth) -> auth.disable());
////        http
////                .httpBasic((auth -> auth.disable()));
////
////
////        http
////                .authorizeHttpRequests((auth) -> auth
////                        .requestMatchers("/jwt-login", "/jwt-login/", "/jwt-login/login", "/jwt-login/join").permitAll()
////                        .requestMatchers("/jwt-login/admin").hasRole("ADMIN")
////                        .anyRequest().authenticated()
////                );
////
////        http
////                .sessionManagement((session) -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
////
////        http
////                .addFilterAt(new LoginFilter(authenticationManager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
////
////        http
////                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
//        // ========= oauth login =========== //
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/oauth-login/admin").hasRole(MemberRole.ADMIN.name())
//                        .requestMatchers("/oauth-login/info").authenticated()
//                        .anyRequest().permitAll()
//                );
//
//        http
//                .formLogin((auth) -> auth.loginPage("/oauth-login/login")
//                        .loginProcessingUrl("/oauth-login/loginProc")
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/oauth-login")
//                        .failureUrl("/oauth-login")
//                        .permitAll());
//
//        http
//                .oauth2Login((auth) -> auth.loginPage("/oauth-login/login")
//                        .defaultSuccessUrl("/oauth-login")
//                        .failureUrl("/oauth-login/login")
//                        .permitAll());
//
//        http
//                .logout((auth) -> auth
//                        .logoutUrl("/oauth-login/logout"));
//
//        http
//                .csrf((auth) -> auth.disable());
//
//        return http.build();
//    }
//    // bcrpytpasswordencoder
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//
//
//        return new BCryptPasswordEncoder();
//    }
//}