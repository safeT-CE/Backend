package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.FaceRequest;
import com.example.kickboard.kickboard.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/face")
public class FaceController {

    @Autowired
    private FaceService faceService;

    // 앱에 자신의 면허증 - 얼굴 등록
    @PostMapping("/request")
    public <T> ResponseEntity<Map<String, Object>> uploadFile(
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("licenseImage") MultipartFile licenseImage,
                                            @RequestParam("faceImage") MultipartFile faceImage){
        Map<String, Object> response = new HashMap<>();

        Path temDir;
        try{
            temDir = Files.createTempDirectory("uploads");
        }catch (IOException e){
             e.printStackTrace();
             response.put("message", "not found" + licenseImage + faceImage + "and temDir");
             return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 면허증, 얼굴 사진 저장
        try{
            File licenseFile = new File(temDir.toFile(), licenseImage.getOriginalFilename());
            File faceFile = new File(temDir.toFile(), faceImage.getOriginalFilename());

            licenseImage.transferTo(licenseFile);
            faceImage.transferTo(faceFile);

            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/detection/Detection/picNface.py",
                                                            licenseFile.getAbsolutePath(),
                                                            faceFile.getAbsolutePath(),
                                                            String.valueOf(userId));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            //
            // 표준 출력과 오류 출력 스트림 읽기
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 표준 출력 로그
            String line;
            while ((line = outputReader.readLine()) != null) {
                System.out.println("Python output: " + line);
            }

            // 오류 출력 로그
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Python error: " + line);
            }

            //
            int exitCode =  process.waitFor();

            // 얼굴, 신분증 사진 삭제
            Files.delete(licenseFile.toPath());
            Files.delete(faceFile.toPath());

            if(exitCode == 0){
                response.put("message", "success");
                log.info("faceController1");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else{
                response.put("message", "Can't store the CSV file");
                log.info("faceController2");
                return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
            }
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            log.info("faceController3");
            response.put("message", "Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 테스트용 [앱에 자신의 면허증 - 얼굴 등록]
    @GetMapping("/upload/test")
    public <T> ResponseEntity<Map<String, Object>> startTestFile(
            @RequestParam("userId") Long userId){
        Map<String, Object> response = new HashMap<>();

        Path temDir;
        try{
            temDir = Files.createTempDirectory("uploads");
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 면허증, 얼굴 동일성 확인
        try{
            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/detection/Detection/test.py",
                    String.valueOf(userId));

            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            int exitCode =  process.waitFor();
            if(exitCode == 0){
                response.put("message", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else{
                response.put("message", "Can't store the CSV file");
                return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
            }
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            response.put("message", "Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // AWS S3에 저장된 csv 경로 받기
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>>  uploadTestFile(@RequestParam("userId") Long userId, @RequestBody FaceRequest request){
        try{
            faceService.saveIdentity(userId, request);
            log.info("faceController4");
            Map<String, Object> response = new HashMap<>();
            response.put("userId", request.getUserId());
            response.put("identity", request.getIdentity());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            log.info("faceController5");
            return ResponseEntity.status(500).body(Map.of("error", "Failed to register Identity to " + userId));
        }
    }
}
