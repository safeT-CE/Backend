package com.example.kickboard.login.service;

import com.example.kickboard.login.dto.JoinRequest;
import com.example.kickboard.login.dto.LoginRequest;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * loginId 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkPhoneDuplicate(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 회원가입 기능 1
     * 화면에서 JoinRequest(phone...)을 입력받아 User로 변환 후 저장
     * phone 등 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    public void join(JoinRequest req) {
        userRepository.save(req.toEntity());
    }

    /**
     *  로그인 기능
     *  화면에서 LoginRequest(phone)을 입력받아  일치하면 User return
     *  phone가 존재하지 않거나 일치하지 않으면 null
     */
    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByPhone(req.getPhone());
        log.info("login : " + req.getPhone());
        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        return user;
    }

    /**
     * phone(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * phone이 null이거나(로그인 X) phone이 찾아온 User가 없으면 null return
     * phone이 찾아온 User가 존재하면 User return
     */
    public User getLoginUserByPhone(String phone) {
        if(phone == null) return null;

        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
}