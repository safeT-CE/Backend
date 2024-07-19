package com.example.kickboard.kickboard.service;


import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class FaceService {

    @Autowired
    private UserRepository userRepository;

    // CSV 파일을 데이터베이스에 저장하는 메서드
    public void saveCSVToDatabase(String csvFilePath, Long userId) throws IOException {
        // userRepository의 userId에 맞는 db identity에 csvFilePath 저장
    }
}