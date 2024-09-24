package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.RentalDetailResponse;
import com.example.kickboard.kickboard.entity.PMap;
import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.kickboard.repository.RentalRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalDetailService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

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
        RentalDetailResponse dto = new RentalDetailResponse();
        dto.setId(rental.getId());
        dto.setKickboardId(rental.getKickboardId());
        dto.setUserId(rental.getUser().getId());
        dto.setRentedAt(rental.getRentedAt());
        dto.setReturned(rental.getReturned());
        dto.setReturnedAt(rental.getReturnedAt());
        dto.setRentalLocation(rental.getRentalLocation());
        dto.setReturnLocation(rental.getReturnLocation());

        return dto;
    }
}
