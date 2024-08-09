package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.FaceRequest;
import com.example.kickboard.kickboard.exception.UserIdNotFoundException;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaceService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Autowired
    public FaceService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public void saveIdentity(Long userId, FaceRequest request) {
        if (userId != null && userId.equals(request.getUserId())) {
            String currentIdentity = userRepository.findIdentityById(userId);
            if(currentIdentity!= null && !currentIdentity.isBlank()){ // 재등록할 경우
                s3Service.deleteImage(currentIdentity);
            }
            // userId로 찾은 user의 idenetity 데이터를 넣기
            userRepository.updateIdentityStatus(userId, request.getIdentity());
        } else {
            throw new UserIdNotFoundException("The UserId in the request doesn't match the UserId in the response.");
        }
    }
}