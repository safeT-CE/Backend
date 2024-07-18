package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.service.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/face")
public class FaceController {

    @Autowired
    private CSVService csvService;

    @PostMapping("/first")
    public ResponseEntity<String> runPythonAndSaveCSV(@RequestParam("userId") Long userId) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/FaceRecognition_JE/picNface.py");
            Process process = pb.start();

            // Python 스크립트 실행 대기
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // CSV 파일을 데이터베이스에 저장
                csvService.saveCSVToDatabase("C:/Users/SAMSUNG/Desktop/FaceRecognition_JE/output/face_features_output.csv", userId);
                return ResponseEntity.ok("CSV file processed and data saved to database.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Python script execution failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred: " + e.getMessage());
        }
    }

}
