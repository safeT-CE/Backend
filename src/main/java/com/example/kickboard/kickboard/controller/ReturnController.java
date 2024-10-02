package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kickboard/return")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    // 킥보드 반납
    @PostMapping
    public ResponseEntity<String> returnKickboard(@RequestParam Long kickboardId) {
        try {
            String result = returnService.returnKickboard(kickboardId);
            if ("Kickboard returned successfully".equals(result)) {
                return ResponseEntity.ok("Kickboard successfully returned.");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while returning the kickboard.");
        }
    }
}