package com.example.kickboard.login.repository;

import com.example.kickboard.kickboard.dto.ProfileResponse;
import com.example.kickboard.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);
    Optional<User> findById(Long id);

    @Query("SELECT u.ticket FROM User u WHERE u.id = :id")
    Boolean findTicketById(@Param("id") Long id);

    @Query("SELECT u.identity FROM User u WHERE u.id = :id")
    String findIdentityById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.ticket = :ticket WHERE u.id = :id")
    void updateTicketStatus(@Param("id") Long id, @Param("ticket") boolean ticket);

    @Modifying
    @Query("UPDATE User u SET u.identity = :identity WHERE u.id = :id")
    void updateIdentityStatus(@Param("id") Long id, @Param("identity") String identity);

}
