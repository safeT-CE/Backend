package com.example.safeT.login.controller;

import com.example.safeT.login.dto.JoinRequest;
import com.example.safeT.login.dto.LoginRequest;
import com.example.safeT.login.entity.User;
import com.example.safeT.login.service.UserService;
import com.example.safeT.login.config.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
            return "The phone number already exists";
        }else if(joinRequest.getPhone().contains("-")){
            throw new IllegalArgumentException("Phone number should not contain hyphens");
        }
        userService.join(joinRequest);
        return "Registration successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);

        if(loginRequest.getPhone().contains("-")){
            throw new IllegalArgumentException("Phone number should not contain hyphens");
        }else if(user == null) {    // 등록 안된 phone
            return "Phone number is not registered";
        }

        // 로그인 성공 시, JWT TOKEN 발급
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getPhone(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByPhone(auth.getName());

        return String.format("Id : %s\nphone : %s\ngrade : %s",
                loginUser.getId(), loginUser.getPhone(), loginUser.getGrade().name());
    }
}
