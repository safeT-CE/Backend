package com.example.kickboard.login.repository;

import com.example.kickboard.kickboard.dto.ProfileResponse;
import com.example.kickboard.login.entity.User;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.identity = :identity, u.samePerson = :samePerson WHERE u.id = :id")
    void updateIdentityStatus(@Param("id") Long id, @Param("identity") String identity, @Param("samePerson") String samePerson);

    @Query("SELECT u.samePerson FROM User u WHERE u.id = :id")
    String findSamePersonById(@Param("id") Long id);

    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    Long findIdByPhone(@Param("phone") String phone);
}
