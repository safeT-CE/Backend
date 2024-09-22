package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.HomeResponse;
import com.example.kickboard.kickboard.dto.ProfileResponse;
import com.example.kickboard.kickboard.exception.UserIdNotFoundException;
import com.example.kickboard.login.entity.Grade;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final UserRepository userRepository;

    public ProfileResponse getProfileInfo(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // User 객체에서 필요한 데이터 추출
            String phone = user.getPhone();
            Grade grade = user.getGrade();
            int point = user.getPoint();
            LocalTime useTime = user.getUseTime();

            // ProfileResponse 객체 생성
            return new ProfileResponse(phone, grade, point, useTime);
        } else {
            // 사용자를 찾지 못한 경우 예외 처리 (예: 사용자 없음 예외 발생)
            throw new UserIdNotFoundException("User not found with id: " + id);
        }
    }

    public HomeResponse getHomeInfo(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // User 객체에서 필요한 데이터 추출
            String phone = user.getPhone();
            Grade grade = user.getGrade();
            LocalTime useTime = user.getUseTime();

            // ProfileResponse 객체 생성
            return new HomeResponse(phone, grade, useTime);
        } else {
            // 사용자를 찾지 못한 경우 예외 처리 (예: 사용자 없음 예외 발생)
            throw new UserIdNotFoundException("User not found with id: " + id);
        }
    }

}
