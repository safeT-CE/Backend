package com.example.kickboard.kickboard.service;


import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;


@Service
public class FaceService {

    @Autowired
    private UserRepository userRepository;

    // CSV 파일을 데이터베이스에 저장하는 메서드
    public void saveCSVToDatabase(String csvFilePath, Long userId) throws IOException {
        // userRepository의 userId에 맞는 db identity에 csvFilePath 저장
    }

    @Transactional
    public void saveIdentity(Long userId, String identity) {
        // userId로 찾은 user의 idenetity 데이터를 넣기
        userRepository.updateIdentityStatus(userId, identity);


    }
}