package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.PenaltyRequest;
import com.example.kickboard.kickboard.entity.Penalty;
import com.example.kickboard.kickboard.service.PenaltyService;
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

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkPenalty(@RequestParam("userId") Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Penalty> penalties = penaltyService.getPenaltiesByUserId(userId);

            if (penalties.isEmpty()) {
                response.put("message", "No penalty points found for the given userId.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", penalties);
            response.put("message", "Penalty points were successfully searched in DB");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "An error occurred while processing the request.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Penalty 추가 : 임시로 만들어 놓음
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
