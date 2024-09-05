package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.dto.ProfileResponse;
import com.example.kickboard.kickboard.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> ticketInfo(@PathVariable("id") Long id){
        ProfileResponse profile = profileService.getUser(id);
        // Map 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("phone", profile.getPhone());
        response.put("grade", profile.getGrade());
        response.put("point", profile.getPoint());
        response.put("useTime", profile.getUseTime());
        // 필요한 다른 정보를 추가할 수 있습니다.

        // ResponseEntity에 Map을 담아서 반환
        return ResponseEntity.ok(response);
    }
}
