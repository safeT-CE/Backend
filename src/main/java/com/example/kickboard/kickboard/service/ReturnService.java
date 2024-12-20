package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Kickboard;
import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.kickboard.repository.KickboardRepository;
import com.example.kickboard.kickboard.repository.RentalRecordRepository;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReturnService {

    @Autowired
    private KickboardRepository kickboardRepository;

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    private final String DETECTION_API_URL = "http://localhost:5000/detect";

    // 킥보드 반납 확인
    @Transactional
    public String checkKickboard(String userStrId, String kickboardStrId) {
        Long kickboardId = Long.parseLong(kickboardStrId);
        Long userId = Long.parseLong(userStrId);
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);

        if (kickboardOptional.isEmpty()) {
            //return "Kickboard not found";
            return "kickboard error";
        }

        Kickboard kickboard = kickboardOptional.get();
        // 킥보드가 대여 중인지 확인
        if (!kickboard.getRented()) {
            //return "Kickboard was not rented";
            return "kickboard error";
        }

        // 유저가 킥보드를 빌린 중인지 확인
        boolean rent = userRepository.findRentById(userId);

        if(!rent){
            //return "User didn't borrow the kickboard";
            return "borrow error";
        }
        // 점자 블록 및 횡단보도 감지 API 호출
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(DETECTION_API_URL, null, String.class);

        // 감지 결과에 따른 반납 가능 여부 판단
        if (response.contains("점자 블록 위반") || response.contains("횡단보도 위반")) {
            //return "반납 불가능: 점자 블록 또는 횡단보도 위반 감지";
            return "Detect violation";
        }
        return "Kickboard returnable";
    }


    // 킥보드 반납
    @Transactional
    public String returnKickboard(Long kickboardId) {
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        Kickboard kickboard = kickboardOptional.get();

        // 킥보드 대여 상태 변경
        kickboard.setRented(false);
        kickboardRepository.save(kickboard);

        // 대여 기록을 찾아서 반납 상태로 변경
        Optional<Rental> rentalOptional = rentalRecordRepository.findTopByKickboardIdAndReturnedFalseOrderByRentedAtDesc(kickboardId);
        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            rental.setReturned(true);
            rental.setReturnedAt(LocalDateTime.now());
            rentalRecordRepository.save(rental);
        }

        return "Kickboard returned successfully";
    }
}
