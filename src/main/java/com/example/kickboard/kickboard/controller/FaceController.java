package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@Slf4j
@RequestMapping(value = "/face")
public class FaceController {

    @Autowired
    private FaceService faceService;

    // 앱에 자신의 면허증 - 얼굴 등록
    @PostMapping("/first")
    public ResponseEntity<String> runPythonAndSaveCSV(@RequestParam("userId") Long userId) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/FaceRecognition_JE/picNface.py");
            Process process = pb.start();

            // 표준 출력 및 오류 읽기
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;

            // 표준 출력 읽기
            while ((line = stdInput.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 표준 오류 읽기
            while ((line = stdError.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // Python 스크립트 실행 대기
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // CSV 파일을 데이터베이스에 저장
                faceService.saveCSVToDatabase("C:/Users/SAMSUNG/Desktop/FaceRecognition_JE/face_rec_data/face_features_output.csv", userId);
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
