package com.example.kickboard.kickboard.dto;

import com.example.kickboard.kickboard.entity.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalDetailResponse {
    private Long id;
    private Long kickboardId;
    private Long userId;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
    private Boolean returned;

    private Long duration; // 주행 시간
    private Location rentalLocation; // 대여 장소
    private Location returnLocation; // 반납 장소
}
