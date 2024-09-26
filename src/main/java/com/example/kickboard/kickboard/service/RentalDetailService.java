package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.KakaoApiResponse;
import com.example.kickboard.kickboard.dto.RentalDetailResponse;
import com.example.kickboard.kickboard.entity.Location;
import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.kickboard.exception.CustomException;
import com.example.kickboard.kickboard.repository.RentalRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalDetailService {

    private RentalRepository rentalRepository;
    private UserRepository userRepository;
    @Value("${KAKAO_API_KEY}")
    private String apiKey;
    private final RestTemplate restTemplate;

    @Autowired
    public RentalDetailService(RentalRepository rentalRepository,
                          UserRepository userRepository,
                          RestTemplate restTemplate
    ) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    // 유저의 전체 이용 내역 조회
    public List<RentalDetailResponse> getUserRentalDetails(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Rental> rentals = rentalRepository.findByUser(user);

            return rentals.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // 세부 이용 내역 조회
    public RentalDetailResponse getRentalDetail(Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        return rental.map(this::convertToDTO).orElse(null);
    }

    // Rental 엔티티를 DTO로 변환
    private RentalDetailResponse convertToDTO(Rental rental) {
        // 지번 주소 DTO로 변환
        RentalDetailResponse dto = convertDTO(rental);

        // Rental 엔티티를 DTO로 변환
        dto.setDuration(rental.getDuration());
        dto.setRentedAt(rental.getRentedAt());
        dto.setRentalLocation(rental.getRentalLocation());
        dto.setReturnLocation(rental.getReturnLocation());


        return dto;
    }

    private RentalDetailResponse convertDTO(Rental rental){
        // 위도, 경도 -> 지번 주소 알아내기
        Location rentalMap = rental.getRentalLocation();
        Location returnMap = rental.getReturnLocation();

        ResponseEntity<KakaoApiResponse> rentalResponse =
                getAddressFromCoordinates(rentalMap.getLatitude(), rentalMap.getLongitude());
        ResponseEntity<KakaoApiResponse> returnResponse =
                getAddressFromCoordinates(returnMap.getLatitude(), returnMap.getLongitude());

        String rentalAddress = getLocation(rentalResponse);
        String returnAddress = getLocation(returnResponse);

        RentalDetailResponse dto = new RentalDetailResponse();
        dto.setRentalAddress(rentalAddress);
        dto.setReturnAddress(returnAddress);
        return dto;
    }

    // 위도, 경도 -> 지번 주소 (PMap -> Penalty.location)
    public ResponseEntity<KakaoApiResponse> getAddressFromCoordinates(double latitude, double longitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("x", longitude)
                .queryParam("y", latitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                KakaoApiResponse.class
        );
        //log.info("PenaltyService : " + response.getBody());
        return response;
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
}
