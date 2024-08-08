package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.FaceRequest;
import com.example.kickboard.kickboard.exception.UserIdNotFoundException;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaceService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveIdentity(Long userId, FaceRequest request) {
        if (userId == request.getUserId()) {
            // userId로 찾은 user의 idenetity 데이터를 넣기
            userRepository.updateIdentityStatus(userId, request.getIdentity());
        } else {
            throw new UserIdNotFoundException("The UserId in the request doesn't match the UserId in the response.");
        }
    }
}