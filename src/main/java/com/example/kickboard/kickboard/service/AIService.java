package com.example.kickboard.kickboard.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@EnableAsync
public class AIService {

    // 효 : 얼굴 동일성 flask 요청 보내기
    public ResponseEntity<Map<String, Object>> sendToPython(Long userId, String faceFile) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/face-detection";

        // 요청 본문 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("userId", userId.toString());
        requestBody.add("faceFile", new FileSystemResource(faceFile)); // faceFile이 실제 파일 경로일 경우

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                flaskUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );
        return response;
    }

    // 파이썬 코드 실행 및 결과 반환
    @Async
    public CompletableFuture<String> executePythonScript(String scriptPath, String userId) {
        StringBuilder result = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, userId);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return CompletableFuture.completedFuture(result.toString());
            } else {
                return CompletableFuture.completedFuture("Error executing Python script.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("Exception occurred: " + e.getMessage());
        }
    }

    // 헬멧 감지 기능 호출
    @Async
    public CompletableFuture<String> detectHelmet(String userId) {
        return executePythonScript("C:/Users/SAMSUNG/Desktop/detection/Detection/realtime_helmet_detection.py", userId);
    }

    // 2인 이상 탑승 감지 기능 호출
    @Async
    public CompletableFuture<String> detectPeople(String userId) {
        return executePythonScript("C:/Users/SAMSUNG/Desktop/detection/Detection/realtime_person_detection.py", userId);
    }

    // 횡단보도 감지 기능 호출
    @Async
    public CompletableFuture<String> detectCrosswalk(String userId) {
        return executePythonScript("C:/Users/SAMSUNG/Desktop/detection/Detection/crosswalk_detection.py", userId);
    }
}