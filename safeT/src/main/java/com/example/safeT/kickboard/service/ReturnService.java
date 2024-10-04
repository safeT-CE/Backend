package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Kickboard;
import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.kickboard.entity.Location;
import com.example.kickboard.kickboard.entity.ReturnLocation;
import com.example.kickboard.kickboard.repository.KickboardRepository;
import com.example.kickboard.kickboard.repository.RentalRecordRepository;
import com.example.kickboard.login.repository.UserRepository;
import com.example.kickboard.kickboard.util.DistanceCalculator;
import com.example.safeT.kickboard.dto.KakaoApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnService {

    @Autowired
    private KickboardRepository kickboardRepository;

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${detection.api.url}")
    private String detectionApiUrl;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${kakao.app.key}")
    private String kakaoAppKey;

    // 반납 가능 지역 리스트
    private List<ReturnLocation> validReturnAreas = Arrays.asList(
            new ReturnLocation(37.5665, 126.978, 1.0), // 중구 세종대로 39 (대한상공회의소) (반경 1km)
            new ReturnLocation(37.654, 127.034, 1.0)   // 도봉구 삼양로144길 33 (덕성여자대학교) (반경 1km)
    );

    // 킥보드 반납 확인
    @Transactional
    public String checkKickboard(String userStrId, String kickboardStrId, Location location) {
        Long kickboardId = Long.parseLong(kickboardStrId);
        Long userId = Long.parseLong(userStrId);

        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        if (kickboardOptional.isEmpty()) {
            return "kickboard error";
        }

        Kickboard kickboard = kickboardOptional.get();

        // 킥보드가 대여 중인지 확인
        if (!kickboard.getRented()) {
            return "kickboard error";
        }

        // 유저가 킥보드를 빌린 중인지 확인
        boolean rent = userRepository.findRentById(userId);
        if (!rent) {
            return "borrow error";
        }

        // 반납 가능 지역 확인
        boolean isValidReturnLocation = validReturnAreas.stream().anyMatch(area ->
                DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), area.getLatitude(), area.getLongitude()) <= area.getRadius()
        );

        if (!isValidReturnLocation) {
            return "Return not allowed outside the designated area";
        }

        // GPS 위치 기반 카카오 API 호출
        RestTemplate restTemplate = new RestTemplate();
        KakaoApiResponse kakaoResponse = restTemplate.getForObject(
                kakaoApiUrl + "?x=" + location.getLongitude() + "&y=" + location.getLatitude(),
                KakaoApiResponse.class
        );

        // Kakao API 응답을 기반으로 반납 가능한 위치 확인
        if (kakaoResponse != null && kakaoResponse.getDocuments() != null && !kakaoResponse.getDocuments().isEmpty()) {
            String addressName = kakaoResponse.getDocuments().get(0).getAddress().getAddressName();
            if (addressName != null && !addressName.isEmpty()) {
                // 점자 블록 및 횡단보도 감지 API 호출
                String response = restTemplate.postForObject(detectionApiUrl, null, String.class);

                // 감지 결과에 따른 반납 가능 여부 판단
                if (response.contains("점자 블록 위반") || response.contains("횡단보도 위반")) {
                    return "Detect violation";
                }
                return "Kickboard returnable";
            }
        }

        return "Invalid return location"; // 반납 장소 유효성 체크 실패
    }

    // 킥보드 반납
    @Transactional
    public String returnKickboard(Long kickboardId) {
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        if (kickboardOptional.isEmpty()) {
            return "kickboard error";
        }

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