package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.service.FaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @GetMapping("/upload")
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

            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/detection/Detection/test.py",
                                                            licenseFile.getAbsolutePath(),
                                                            faceFile.getAbsolutePath(),
                                                            String.valueOf(userId));

            pb.redirectErrorStream(true);

            Process process = pb.start();


            // Python 스크립트의 출력 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    //output.append(line);
                }
            }

            int exitCode =  process.waitFor();

            Files.delete(licenseFile.toPath());
            Files.delete(faceFile.toPath());

            if(exitCode == 0){
                String identity = output.toString().trim();

                // 응답
                response.put("message", "success");
                response.put("identity", identity);
                faceService.saveIdentity(userId, identity);

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

    // 테스트용
    @GetMapping("/upload/test")
    public <T> ResponseEntity<Map<String, Object>> uploadTestFile(
            @RequestParam("userId") Long userId){
        Map<String, Object> response = new HashMap<>();

        Path temDir;
        try{
            temDir = Files.createTempDirectory("uploads");
        }catch (IOException e){
            e.printStackTrace();
            //response.put("message", "not found" + licenseImage + faceImage + "and temDir");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 면허증, 얼굴 사진 저장
        try{
            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/detection/Detection/test.py",
                    String.valueOf(userId));

            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Python 스크립트의 출력 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line); //.append("\n");
                    //output.append(line);
                }
            }

            int exitCode =  process.waitFor();
            if(exitCode == 0){
                String identity = output.toString().trim();
                log.info("FaceController : 1"); //
                log.info("FaceController : " + identity);

                // 처리된 JSON 응답
                response.put("message", "success");
                response.put("identity", identity);
                faceService.saveIdentity(userId, identity);  // 저장 로직에 JSON 응답 전달
                log.info("FaceController : 2"); //

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
}
