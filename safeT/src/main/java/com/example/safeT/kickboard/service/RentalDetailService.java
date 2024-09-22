package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.dto.RentalDetailDTO;
import com.example.safeT.kickboard.entity.Rental;
import com.example.safeT.kickboard.entity.Location;
import com.example.safeT.kickboard.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalDetailService {

    @Autowired
    private RentalRepository rentalRepository;

    // 유저의 전체 이용 내역 조회
    public List<RentalDetailDTO> getUserRentalDetails(Long userId) {
        // 유저 ID로 이용 내역 조회
        List<Rental> rentals = rentalRepository.findByUser(new User(userId));
        return rentals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 세부 이용 내역 조회
    public RentalDetailDTO getRentalDetail(Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        return rental.map(this::convertToDTO).orElse(null);
    }

    // Rental 엔티티를 DTO로 변환
    private RentalDetailDTO convertToDTO(Rental rental) {
        RentalDetailDTO dto = new RentalDetailDTO();
        dto.setId(rental.getId());
        dto.setKickboardId(rental.getKickboardId());
        dto.setUserId(rental.getUser().getId());
        dto.setRentedAt(rental.getRentedAt());
        dto.setReturned(rental.getReturned());
        dto.setReturnedAt(rental.getReturnedAt());
        dto.setRentalLocation(rental.getRentalLocation());
        dto.setReturnLocation(rental.getReturnLocation());

        // 주행 거리 계산
        if (rental.getStartLocation() != null && rental.getReturnedAt() != null) {
            PMap startLocation = rental.getStartLocation(); // 대여 장소
            PMap endLocation = rental.getEndLocation(); // 반납 장소
            double distance = DistanceCalculator.calculateDistance(startLocation, endLocation);
            dto.setDistance(distance); // DTO에 거리 추가
        }
        return dto;
    }
}
