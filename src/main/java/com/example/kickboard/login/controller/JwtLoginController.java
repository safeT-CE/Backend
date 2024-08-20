package com.example.kickboard.login.controller;

import com.example.kickboard.login.dto.JoinRequest;
import com.example.kickboard.login.dto.LoginRequest;
import com.example.kickboard.login.dto.LoginResponse;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.service.UserService;
import com.example.kickboard.login.config.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/auth", produces= "application/json")
public class JwtLoginController {

    @Autowired
    private UserService userService;

    @Value("${JWT_SECRET}")
    private String secretKey;

    // 회원가입
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);

        if(loginRequest.getPhone().contains("-")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
                    //.body("Phone number should not contain hyphens");
        }else if(user == null) {    // 등록 안된 phone
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
                    //.body("Phone number is not registered");
        }

        // 로그인 성공 시, JWT TOKEN 발급
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        String jwtToken = JwtTokenUtil.createToken(user.getPhone(), secretKey, expireTimeMs);
        Long userId = user.getId();
        LoginResponse loginResponse = new LoginResponse(jwtToken, userId);

        return ResponseEntity.ok(loginResponse);
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
