package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.dto.HomeResponse;
import com.example.kickboard.kickboard.dto.ProfileResponse;
import com.example.kickboard.kickboard.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/", produces = "application/json; charset=UTF-8")
public class InfoController {

    private final InfoService profileService;

    @GetMapping("profile/{id}")
    public ResponseEntity<Map<String, Object>> profileInfo(@PathVariable("id") Long id){
        ProfileResponse profile = profileService.getProfileInfo(id);
        // Map 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("phone", profile.getPhone());
        response.put("grade", profile.getGrade());
        response.put("point", profile.getPoint());
        response.put("useTime", profile.getUseTime());

        return ResponseEntity.ok(response);
    }

    @GetMapping("home/{id}")
    public ResponseEntity<Map<String, Object>> homeInfo(@PathVariable("id") Long id){
        HomeResponse home = profileService.getHomeInfo(id);
        // Map 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("phone", home.getPhone());
        response.put("grade", home.getGrade());
        response.put("useTime", home.getUseTime().toString());

        return ResponseEntity.ok(response);
    }
}
