package com.example.safeT.kickboard.controller;

import com.example.safeT.kickboard.dto.PenaltyDetailResponse;
import com.example.safeT.kickboard.dto.PenaltyRequest;
import com.example.safeT.kickboard.dto.PenaltySummaryResponse;
import com.example.safeT.kickboard.entity.Penalty;
import com.example.safeT.kickboard.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/penalty")
public class PenaltyController {

    @Autowired
    private PenaltyService penaltyService;

    // 전체 조회
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkPenalty(@RequestParam("userId") Long userId) {
        List<Penalty> penalties = penaltyService.getPenaltiesByUserId(userId);
        return createResponse(penalties, "All penalty information");
    }

    // 기본 조회
    @GetMapping("/check/summary")
    public ResponseEntity<Map<String, Object>> getPenaltySummaries(@RequestParam("userId") Long userId){
        List<PenaltySummaryResponse> penalties = penaltyService.getPenaltySummaries(userId);
        return createResponse(penalties, "penalty");
    }

    // 상세 조회
    @GetMapping("/check/detail")
    public ResponseEntity<Map<String, Object>> getPenaltyDetail(@RequestParam("userId") Long userId, @RequestParam("penaltyId")Long penaltyId) {
        List<PenaltyDetailResponse> penalties = penaltyService.getPenaltyDetail(userId, penaltyId);
        //List<PenaltyDetailResponse> penalties = penaltyService.getPenaltyDetail(userId);
        return createResponse(penalties, "penalty");
    }



    private <T> ResponseEntity<Map<String, Object>> createResponse(List<T> penalties, String itemName) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (penalties.isEmpty()) {
                response.put("message", "No " + itemName + " points found for the given userId.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put(itemName, penalties);
            response.put("message", itemName + " points were successfully searched in DB");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "An error occurred while processing the request.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Penalty 추가
    @PostMapping
    public ResponseEntity<?> createPenalty(@RequestParam("userId") Long userId, @RequestBody PenaltyRequest request) {
        try {
            Penalty penalty = penaltyService.createPenalty(userId, request);
            return new ResponseEntity<>(penalty, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
