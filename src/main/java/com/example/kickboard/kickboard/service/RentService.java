package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Kickboard;
import com.example.kickboard.kickboard.entity.Penalty;
import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.kickboard.repository.KickboardRepository;
import com.example.kickboard.kickboard.repository.PenaltyRepository;
import com.example.kickboard.kickboard.repository.RentalRecordRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RentService {

    @Autowired
    private KickboardRepository kickboardRepository;

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    // 킥보드 QR 코드로 조회
    public Kickboard getKickboardByQrCode(String qrCode) {
        return kickboardRepository.findByQrCode(qrCode);
    }
    // 킥보드 대여 ID로 조회
    public Kickboard getKickboardByRentalId(Long rentalId) {
        Optional<Rental> rentalOptional = rentalRecordRepository.findById(rentalId);
        return rentalOptional.map(rental -> kickboardRepository.findById(rental.getKickboardId()).orElse(null)).orElse(null);
    }

    // 킥보드 대여
    @Transactional
    public String rentKickboard(Long kickboardId, Long userId, Long penaltyId) {
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        if (kickboardOptional.isEmpty()) {
            return "Kickboard not found";
        }

        Kickboard kickboard = kickboardOptional.get();

        // 킥보드 대여 여부 조회
        if (kickboardRepository.findRentedById(kickboardId)) {
            return "Kickboard is not available";
        }

        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "User not found";
        }

        // 벌점 존재 여부 확인
        Penalty penalty = penaltyRepository.findById(penaltyId).orElse(null);
        if (penalty == null) {
            return "Penalty not found";
        }

        // 킥보드 대여 상태 변경
        kickboard.setRented(true);
        kickboardRepository.save(kickboard);

        // 대여 기록 생성
        Rental rental = new Rental();
        rental.setKickboardId(kickboardId);
        rental.setUser(user);   // 수정
        rental.setPenaltyId(penaltyId);
        rental.setRentedAt(LocalDateTime.now());
        rental.setReturned(false);
        rentalRecordRepository.save(rental);

        return "Kickboard rented successfully";
    }

    public boolean validateRentalByQrCode(String qrCode) {
        if (kickboardRepository.findRentedByQrCode(qrCode) || kickboardRepository.findByQrCode(qrCode) == null)
            return false;
        return true;
    }

    public boolean validateRentalByModel(Long model) {
        if (kickboardRepository.findRentedByModel(model) || kickboardRepository.findByModel(model) == null)
            return false;
        return true;
    }

    public String findIdentity(Long userId) {
        return userRepository.findIdentityById(userId);
    }
}