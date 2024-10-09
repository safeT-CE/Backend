package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.exception.TicketAlreadyUpdatedException;
import com.example.kickboard.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
