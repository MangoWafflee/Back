package com.example.MangoWafflee.Controller;

import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/loginSuccess")
    public ModelAndView loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        UserDTO userDTO = userService.handleKakaoLogin(oAuth2User);
        return new ModelAndView("loginSuccess").addObject("user", userDTO);
    }

    @GetMapping("/loginFailure")
    public ModelAndView loginFailure() {
        return new ModelAndView("loginFailure");
    }
}
