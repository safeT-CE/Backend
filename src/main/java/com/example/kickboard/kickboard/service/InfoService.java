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
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final UserRepository userRepository;

    public ProfileResponse getProfileInfo(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return convertToDto(user);
        } else {
            // 사용자를 찾지 못한 경우
            throw new UserIdNotFoundException("User not found with id: " + id);
        }
    }

    public static ProfileResponse convertToDto(User user) {
        // User 객체에서 필요한 데이터 추출
        String phone = user.getPhone();
        Grade grade = user.getGrade();
        int point = user.getPoint();
        LocalTime useTime = user.getUseTime();

        // 날짜 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H시간m분");

        // ProfileResponse 객체 생성
        return new ProfileResponse(phone, grade, point, useTime.format(formatter));
    }

    public HomeResponse getHomeInfo(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String phone = user.getPhone();
            Grade grade = user.getGrade();
            LocalTime useTime = user.getUseTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H시간m분");

            return new HomeResponse(phone, grade, useTime.format(formatter));
        } else {
            throw new UserIdNotFoundException("User not found with id: " + id);
        }
    }

}
