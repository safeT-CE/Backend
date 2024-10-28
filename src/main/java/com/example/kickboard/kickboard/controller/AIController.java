package com.example.kickboard.kickboard.controller;
import com.example.kickboard.kickboard.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    // 헬멧 감지 요청 처리
    @GetMapping("/helmet-detection")
    public ResponseEntity<String> helmetDetection(@RequestParam String userId) {
        try {
            String result = String.valueOf(aiService.detectHelmet(userId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // 2인 이상 탑승 감지 요청 처리

    @GetMapping("/people-detection")
    public ResponseEntity<String> peopleDetection(@RequestParam String userId) {
        try {
            String result = String.valueOf(aiService.detectPeople(userId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // 횡단보도 감지 요청 처리
//    @GetMapping("/crosswalk-detection")
//    public ResponseEntity<String> crosswalkDetection(@RequestParam String userId) {
//        try {
//            String result = aiService.detectCrosswalk(userId);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
//        }
//    }
}
