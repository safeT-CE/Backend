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

    // 킥보드 반납 가능 여부 확인
    @PostMapping("/check")
    public ResponseEntity<String> checkReturnKickboard(@RequestBody String userId, @RequestBody String kickboardId){
        try {
            String result = returnService.checkKickboard(userId, kickboardId);
            if ("Kickboard returnable".equals(result)) {
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while returning the kickboard.");
        }
    }

    // 킥보드 반납
    @PostMapping("")
    public ResponseEntity<String> returnKickboard(@RequestParam("kickboardId") Long kickboardId) {
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