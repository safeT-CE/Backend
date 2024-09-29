package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.entity.Kickboard;
import com.example.safeT.kickboard.entity.Penalty;
import com.example.safeT.kickboard.entity.Rental;
import com.example.safeT.kickboard.entity.Location;
import com.example.safeT.login.entity.User;
import com.example.safeT.kickboard.repository.KickboardRepository;
import com.example.safeT.kickboard.repository.PenaltyRepository;
import com.example.safeT.kickboard.repository.RentalRepository;
import com.example.safeT.login.repository.UserRepository;
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
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    // QR 코드로 킥보드 상세 정보 조회
    public Kickboard getKickboardByQrCode(String qrCode) {
        return kickboardRepository.findByQrCode(qrCode);
    }

    // 대여 번호로 킥보드 상세 정보 조회
    public Kickboard getKickboardByRentalId(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental != null) {
            return kickboardRepository.findById(rental.getKickboardId()).orElse(null);
        }
        return null;
    }

    // 킥보드 대여
    @Transactional
    public String rentKickboard(Long kickboardId, Long userId, Long penaltyId, PMap startLocation) {
        // 킥보드 존재 여부 확인
        Optional<Kickboard> kickboardOptional = kickboardRepository.findById(kickboardId);
        if (kickboardOptional.isEmpty()) {
            return "Kickboard not found";
        }

        Kickboard kickboard = kickboardOptional.get();

        // 킥보드 대여 여부 확인
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
        rental.setUserId(userId);
        rental.setPenaltyId(penaltyId);
        rental.setRentedAt(LocalDateTime.now());
        rental.setReturned(false);
        rentalRepository.save(rental);

        return "Kickboard rented successfully";
    }

    // 킥보드 반납
    @Transactional
    public String returnKickboard(Long kickboardId, Location endLocation) {
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

        // 킥보드 대여 상태 변경
        kickboard.setRented(false);
        kickboardRepository.save(kickboard);

        // 대여 기록을 찾아서 반납 상태로 변경
        Optional<Rental> rentalOptional = rentalRepository.findTopByKickboardIdAndReturnedFalseOrderByRentedAtDesc(kickboardId);
        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            rental.setReturned(true);
            rental.setReturnedAt(LocalDateTime.now());
            rentalRepository.save(rental);
        }

        return "Kickboard returned successfully";
    }
}