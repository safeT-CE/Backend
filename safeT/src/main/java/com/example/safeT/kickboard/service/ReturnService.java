package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.entity.Kickboard;
import com.example.safeT.kickboard.entity.Rental;
import com.example.safeT.kickboard.entity.Location;
import com.example.safeT.kickboard.repository.KickboardRepository;
import com.example.safeT.kickboard.repository.RentalRecordRepository;
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

    private final String DETECTION_API_URL = "http://localhost:5000/detect";

    // 킥보드 반납
    @Transactional
    public String returnKickboard(Long kickboardId) {
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        if (kickboardOptional.isEmpty()) {
            return "Kickboard not found";
        }

        Kickboard kickboard = kickboardOptional.get();

        // 킥보드가 대여 중인지 확인
        if (!kickboard.getRented()) {
            return "Kickboard was not rented";
        }

        // 점자 블록 및 횡단보도 감지 API 호출
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(DETECTION_API_URL, null, String.class);

        // 감지 결과에 따른 반납 가능 여부 판단
        if (response.contains("점자 블록 위반") || response.contains("횡단보도 위반")) {
            return "반납 불가능: 점자 블록 또는 횡단보도 위반 감지";
        }

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
