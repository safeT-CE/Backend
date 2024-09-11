package com.example.safeT.login.repository;

import com.example.safeT.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);
    @Query("SELECT u.ticket FROM User u WHERE u.id = :id")
    Boolean findTicketById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.ticket = :ticket WHERE u.id = :id")
    void updateTicketStatus(@Param("id") Long id, @Param("ticket") boolean ticket);

}
