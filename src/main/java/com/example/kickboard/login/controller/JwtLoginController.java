package com.example.kickboard.login.controller;

import com.example.kickboard.login.dto.JoinRequest;
import com.example.kickboard.login.dto.LoginRequest;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.service.UserService;
import com.example.kickboard.login.config.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/login", produces= "application/json")
public class JwtLoginController {

    @Autowired
    private UserService userService;

    @Value("${JWT_SECRET}")
    private String secretKey;


    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest){
        // phone 중복 확인
        if(userService.checkPhoneDuplicate(joinRequest.getPhone())){
            return "로그인 아이디가 중복됩니다.";
        }
        userService.join(joinRequest);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);
        log.info("controller : " + secretKey);
        log.info("controller : " + user);
        // phone 틀린 경우
        if(user == null){
            return "등록되지 않은 전화번호입니다.";
        }

        // 로그인 성공 시, JWT TOKEN 발급
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getPhone(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByPhone(auth.getName());

        return String.format("Id : %s\nphone : %s\ngrade : %s",
                loginUser.getId(), loginUser.getPhone(), loginUser.getGrade().name());
    }
}
