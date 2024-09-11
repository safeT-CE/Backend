package com.example.safeT.kickboard.dto;


public class TicketRequest {
    private Long id;
    private boolean ticket;
    public Long getUserId() { return id; }
    public void setUserId(Long userId) { this.id = userId; }

    public boolean getTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }

    public void buyTicket() {
        this.ticket = true;
    }
}
