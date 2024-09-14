package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.FaceRequest;
import com.example.kickboard.kickboard.exception.UserIdNotFoundException;
import com.example.kickboard.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
@Slf4j
public class FaceService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Autowired
    public FaceService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public String detectFace(Long userId) {
        String samePerson = userRepository.findSamePersonById(userId);
        return samePerson;
    }

    @Transactional
    public void saveIdentity(String user_id, FaceRequest request) {
        Long userId = Long.parseLong(user_id);
        log.info("face service1");
        String currentIdentity = userRepository.findIdentityById(userId);
        log.info("face service2 : {}", currentIdentity);
        if (userId != null) {
            log.info("face service3");
            if (currentIdentity != null && !currentIdentity.isBlank()) { // 재등록할 경우
                log.info("face service4");
                //s3Service.deleteImage(currentIdentity);   // 오류!
            }
            // userId로 찾은 user의 idenetity 데이터를 넣기
            log.info("face service5");
            userRepository.updateIdentityStatus(userId, request.getIdentity(), request.getSamePerson());
            log.info("face service6");
        } else { // CSV 경로 전송 실패 시
            s3Service.deleteImage(currentIdentity);
            throw new UserIdNotFoundException("The UserId in the request doesn't match the UserId in the response.");
        }
    }

    public void checkSamePerson(String samePerson) {
    }
}