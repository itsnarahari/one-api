package com.one.repositories;

import com.one.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByUserMobile(String mobile);
    Optional<Otp> findTopByUserMobileOrderByTimestampDesc(String phoneNumber);

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.timestamp < :cutoff")
    void deleteExpired(@Param("cutoff") LocalDateTime cutoff);
}

