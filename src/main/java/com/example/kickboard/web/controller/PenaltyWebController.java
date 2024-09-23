package com.example.kickboard.web.controller;

import com.example.kickboard.web.dto.PenaltyAllResponse;
import com.example.kickboard.web.dto.PenaltyCountResponse;
import com.example.kickboard.web.service.PenaltyWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/web/penalty/")
public class PenaltyWebController {

    @Autowired
    private PenaltyWebService penaltyWebService;

    //벌점 기록 : safeT 사용자의 전체 조회
    @GetMapping("check")
    public ResponseEntity<Map<String, Object>> checkAllPenalty() {
        Map<String, Object> response = new HashMap<>();
        List<PenaltyAllResponse> penalties = penaltyWebService.getPenalties();
        if (penalties.isEmpty()) {
            response.put("message", "There are no penalty points");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("penalties", penalties);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 벌점 기록 : 통계 각 기능별 횟수 조회
    @GetMapping("count")
    public ResponseEntity<Map<String,Object>> checkContentPenalty() {
        List<PenaltyCountResponse> count = penaltyWebService.getCount();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
