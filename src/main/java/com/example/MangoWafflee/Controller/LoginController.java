package com.example.MangoWafflee.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";  // 템플릿의 login.html을 반환
    }

    @GetMapping("/oauth2/loginSuccess")
    public String loginSuccess() {
        return "redirect:/"; // 로그인 성공 후 리다이렉트할 페이지 설정
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "redirect:/login?error=true";
    }
}
