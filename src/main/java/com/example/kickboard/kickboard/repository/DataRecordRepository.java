package com.example.kickboard.kickboard.repository;


import com.example.kickboard.kickboard.entity.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, String> {

}
