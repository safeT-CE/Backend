package com.example.safeT.kickboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.tine.LocalDateTime;

@Getter
@Setter
public class RentalDetailDTO {
    private Long id;
    private Long kickboardId;
    private Long userId;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
    private Boolean returned;
}
