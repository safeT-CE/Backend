package com.example.kickboard.kickboard.service;


import com.example.kickboard.kickboard.entity.DataRecord;
import com.example.kickboard.kickboard.repository.DataRecordRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.kickboard.dto.DataRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    private DataRecordRepository dataRecordRepository;

    // CSV 파일을 데이터베이스에 저장하는 메서드
    public void saveCSVToDatabase(String csvFilePath, Long userId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // CSV 파일의 각 라인을 읽어서 데이터베이스에 저장
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(","); // CSV 파일의 각 열을 분리

                // DataRecordDto를 사용하여 엔티티로 변환
                DataRecordDto dataRecordDto = new DataRecordDto(cols[0], cols[1], cols[2]);
                DataRecord dataRecord = dataRecordDto.toEntity();

                // User 엔티티와 연결
                User user = new User();
                user.setId(userId); // 주어진 userId로 사용자 설정
                dataRecord.setUser(user);

                // 데이터베이스에 저장
                dataRecordRepository.save(dataRecord);
            }
        }
    }
}