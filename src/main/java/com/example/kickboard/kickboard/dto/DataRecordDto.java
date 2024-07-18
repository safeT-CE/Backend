package com.example.kickboard.kickboard.dto;

import com.example.kickboard.kickboard.entity.DataRecord;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataRecordDto {

    private String feature;
    private String col1;
    private String col2;


    //Schedule 엔티티 반환
    public DataRecord toEntity(){
        return DataRecord.builder()
                .feature(this.feature)
                .col1(this.col1)
                .col2(this.col2)
                .build();
    }
}
