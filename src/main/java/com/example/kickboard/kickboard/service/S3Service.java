package com.example.kickboard.kickboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

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

    // 이미지 삭제 메소드
    public void deleteImage(String fileUrl) {
        try {
            String splitStr = ".com/";
            String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }catch (S3Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete img from AWS S3 : " + e.awsErrorDetails().errorMessage());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while delete img from AWS S3");
        }
    }
}
