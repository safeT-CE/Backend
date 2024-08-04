package com.example.kickboard.kickboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;

@Service
public class S3Service {

    private final S3Client s3Client;

    private final String bucketName = "safet-bucket";

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${AWS_REGION}")
    private String region;

    public String uploadFileToS3(File file){
        try{
            String  fileName = file.getName();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, file.toPath());

            // 파일의 S3 경로 반환
            return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucketName, region, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
