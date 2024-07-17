package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.TicketRequest;
import com.example.kickboard.kickboard.exception.TicketAlreadyUpdatedException;
import com.example.kickboard.kickboard.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/ticket")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> ticketInfo(@PathVariable("id") Long id){
        try {
            boolean ticketStatus = ticketService.getTicketByUserId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("ticketStatus", ticketStatus);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve ticket status"));
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> changeTicketStatus(@RequestBody TicketRequest request){
        try {
            ticketService.buyTicket(request.getUserId());
            request.buyTicket();

            Map<String, Object> response = new HashMap<>();
            response.put("userId", request.getUserId());
            response.put("ticket", request.getTicket());

            return ResponseEntity.ok(response);
        } catch (TicketAlreadyUpdatedException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to change ticket status"));
        }
    }
}
