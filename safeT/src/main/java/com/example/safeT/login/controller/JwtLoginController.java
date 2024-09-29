package com.example.safeT.login.controller;

import com.example.safeT.login.dto.JoinRequest;
import com.example.safeT.login.dto.LoginRequest;
import com.example.kickboard.login.dto.LoginResponse;
import com.example.safeT.login.entity.User;
import com.example.safeT.login.service.UserService;
import com.example.safeT.login.config.jwt.util.JwtTokenUtil;
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

    // 전화번호 유효성 검사
    @GetMapping("/phone")
    public String checkPhone(@RequestParam("phone") String phone){
        // phone 중복 확인
        if(userService.checkPhoneDuplicate(phone)){
            // return "The phone number already exists";
            return "이미 등록된 전화번호입니다.";
        } else if(phone.contains("-")){
            //return "Phone number should not contain hyphens";
            return "전화번호에 하이픈을 포함할 수 없습니다.";
        } else if(!userService.checkphoneType(phone)){
            //return "Invalid phone number format";
            return "유효하지 않은 전화번호 형식입니다.";
        }
        return "Registrationable Number";
    }

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody JoinRequest joinRequest){
        Long userId = userService.join(joinRequest);
        return userId;
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
