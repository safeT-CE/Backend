package com.example.kickboard.kickboard.dto;

import com.example.kickboard.kickboard.entity.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalDetailResponse {
    private Long id;
    private String rentedAt;
    private Long duration; // 주행 시간 (초 단위)
    private Location rentalLocation; // 대여 위도/경도
    private Location returnLocation; // 반납 위도/경도
    private String rentalAddress; // 지번 주소
    private String returnAddress;
}
