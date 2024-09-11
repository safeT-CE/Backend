package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.exception.TicketAlreadyUpdatedException;
import com.example.safeT.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final UserRepository userRepository;


    public boolean getTicketByUserId(Long userId) throws Exception{
        boolean ticketStatus = userRepository.findTicketById(userId);
        return ticketStatus;
    }

    public void buyTicket(Long userId) throws Exception{
        if(userRepository.findTicketById(userId)== true)
            throw new TicketAlreadyUpdatedException("Ticket is already set to 1");
        userRepository.updateTicketStatus(userId, true);
    }
}
