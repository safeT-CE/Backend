package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.dto.PenaltyRequest;
import com.example.kickboard.kickboard.entity.Penalty;
import com.example.kickboard.kickboard.repository.PenaltyRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.kickboard.kickboard.entity.PMap;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;

    @Autowired
    public PenaltyService(PenaltyRepository penaltyRepository, UserRepository userRepository) {
        this.penaltyRepository = penaltyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Penalty> getPenaltiesByUserId(Long userId) {
            return penaltyRepository.findAllByUserId(userId);
    }

    @Transactional
    public Penalty createPenalty(Long userId, PenaltyRequest request){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("Invalid user ID"));

        Penalty penalty = new Penalty();
        // 추후에 Content, Photo, Map -> Location은 받아오는 형식으로 수정할 예정임.
        penalty.setContent(request.getContent());
        penalty.setLocation(request.getLocation());
        penalty.setPhoto(request.getPhoto());

        Map<String, Object> mapData = request.getMap();
        PMap map = new PMap((Double) mapData.get("latitude"), (Double) mapData.get("longitude"));
        penalty.setMap(map);

        penalty.setUser(user);

        // count 필드를 설정하는 로직 추가 (예: 사용자의 페널티 수를 기준으로 설정)
        penalty.setCount((int) penaltyRepository.countByUserId(userId) + 1);

        return penaltyRepository.save(penalty);
    }
}
