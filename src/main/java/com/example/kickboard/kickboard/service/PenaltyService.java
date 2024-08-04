package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.KakaoApiResponse;
import com.example.kickboard.kickboard.dto.PenaltyDetailResponse;
import com.example.kickboard.kickboard.dto.PenaltyRequest;
import com.example.kickboard.kickboard.dto.PenaltySummaryResponse;
import com.example.kickboard.kickboard.entity.Penalty;
import com.example.kickboard.kickboard.exception.CustomException;
import com.example.kickboard.kickboard.repository.PenaltyRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.kickboard.kickboard.entity.PMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;  //

    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public PenaltyService(PenaltyRepository penaltyRepository, UserRepository userRepository, RestTemplate restTemplate, NotificationService notificationService) {
        this.penaltyRepository = penaltyRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService; //
    }

    // 벌점 기록 전체 조회
    @Transactional
    public List<Penalty> getPenaltiesByUserId(Long userId) {
            return penaltyRepository.findByUserId(userId);
    }

    // 벌점 기록 기본 조회
    @Transactional
    public List<PenaltySummaryResponse> getPenaltySummaries(Long userId) {
        List<Penalty> penalties = penaltyRepository.findByUserId(userId);
        return penalties.stream()
                .map(penalty -> new PenaltySummaryResponse(
                        penalty.getContent(),
                        penalty.getDate(),
                        penalty.getTotalCount(),
                        convertPMapToMap(penalty.getMap())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PenaltyDetailResponse> getPenaltyDetail(Long userId) {
        List<Penalty> penalties = penaltyRepository.findByUserId(userId);
        log.info("PenaltyService : " + userId);
        return penalties.stream()
                .map(penalty -> new PenaltyDetailResponse(
                        penalty.getContent(),
                        penalty.getDate(),
                        penalty.getTotalCount(),
                        penalty.getLocation(),
                        convertPMapToMap(penalty.getMap()),
                        penalty.getPhoto(),
                        penalty.getDetectionCount()
                ))
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertPMapToMap(PMap pMap) {
        Map<String, Object> map = new HashMap<>();
        if (pMap != null) {
            map.put("latitude", pMap.getLatitude());
            map.put("longitude", pMap.getLongitude());
        }
        return map;
    }


    // 벌점 기록 생성하기
    @Transactional
    public Penalty createPenalty(Long userId, PenaltyRequest request){
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            Penalty penalty = new Penalty();
            log.info("service ok1");
            // 추후에 Content, Photo, Map -> Location은 받아오는 형식으로 수정할 예정임.
            penalty.setContent(request.getContent());
            //penalty.setLocation(request.getLocation());
            penalty.setPhoto(request.getPhoto());
            penalty.setDate(request.getDate());
            penalty.setDetectionCount(request.getDetectionCount());
            log.info("service ok2");
            Map<String, Object> mapData = request.getMap();
            PMap map = new PMap((Double) mapData.get("latitude"), (Double) mapData.get("longitude"));
            penalty.setMap(map);
            log.info("service ok3");
            // 위도, 경도 -> 지번 주소 알아내기
            ResponseEntity<KakaoApiResponse> response = getAddressFromCoordinates(map.getLatitude(), map.getLongitude());
            String location = getLocation(response);
            penalty.setLocation(location);
            log.info("service ok4");
            // user 최종 set
            penalty.setUser(user);
            log.info("service ok5");
            // count 필드 설정 (userId 기준)
            penalty.setTotalCount((int) penaltyRepository.countByUserId(userId) + 1);

            //
            // 벌점 데이터베이스에 저장
            Penalty savedPenalty = penaltyRepository.save(penalty);

            // 벌점 등록 후 사용자에게 알림 전송
            String message = request.getContent();
            notificationService.notifyUser(userId, message);

            return savedPenalty;
            //

            //return penaltyRepository.save(penalty);
        }catch (CustomException e){
            log.error("Error occurred while created Penalty : ", e);
            throw e;
        } catch (Exception e){
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error occurred : " + e.getMessage());
        }
    }

    private String getLocation(ResponseEntity<KakaoApiResponse> response){
        if(response.getStatusCode().is2xxSuccessful() && !response.getBody().getDocuments().isEmpty()){
            KakaoApiResponse.Document document = response.getBody().getDocuments().get(0);
            return document.getAddress().getAddressName();
        } else if(response.getStatusCode().is4xxClientError()) {
            throw  new CustomException(HttpStatus.BAD_REQUEST, "Client error : " + response.getStatusCode());
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error : " + response.getStatusCode());
        } else {
             throw new CustomException(HttpStatus.CREATED, "Address not found");
        }
    }

    // 위도, 경도 -> 지번 주소 (PMap -> Penalty.location)
    public ResponseEntity<KakaoApiResponse> getAddressFromCoordinates(double latitude, double longitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("x", longitude)
                .queryParam("y", latitude);
        log.info("kakao service ok1");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("kakao service ok2");
        ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                KakaoApiResponse.class
        );

        log.info("PenaltyService : " + response.getBody());
        return response;
    }
}
