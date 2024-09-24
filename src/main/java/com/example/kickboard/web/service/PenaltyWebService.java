package com.example.kickboard.web.service;

import com.example.kickboard.kickboard.dto.PenaltySummaryResponse;
import com.example.kickboard.kickboard.entity.PMap;
import com.example.kickboard.kickboard.entity.Penalty;
import com.example.kickboard.kickboard.repository.PenaltyRepository;
import com.example.kickboard.web.dto.PenaltyAllResponse;
import com.example.kickboard.web.dto.PenaltyCountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.kickboard.kickboard.entity.Category.penalty;

@Service
public class PenaltyWebService {

    @Autowired
    private PenaltyRepository penaltyRepository;

    public List<PenaltyAllResponse> getPenalties() {
        List<Penalty> penalties = penaltyRepository.findAll();

        return penalties.stream()
                .map(penalty -> new PenaltyAllResponse(
                        penalty.getContent(),
                        penalty.getDate(),
                        ((int) penaltyRepository.countAll()),
                        penalty.getLocation(),
                        penalty.getDetectionCount()
                ))
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertPMapToMap(PMap pMap) {
        Map<String, Object> map = new HashMap<>();
        if (pMap != null) {
            map.put("latitude", pMap.getLatitude());
            map.put("longitude", pMap.getLongitude());
        }
        return map;
    }

    public List<PenaltyCountResponse> getCount() {
        List<String> contents = penaltyRepository.findContent();

        Map<String, Integer> contentCountMap = new HashMap<>();
        for (String content : contents) {
            contentCountMap.put(content, contentCountMap.getOrDefault(content, 0) + 1);
        }

        List<PenaltyCountResponse> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : contentCountMap.entrySet()) {
            PenaltyCountResponse response = new PenaltyCountResponse();
            response.setContent(entry.getKey());
            response.setCount(entry.getValue());
            result.add(response);
        }
        return result;
    }
}
