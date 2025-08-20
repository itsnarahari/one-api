package com.one.repositories;

import com.one.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    public List<Otp> findByMobile(String mobile);
    public List<Otp> findAllByMobileOrderByTimestampDesc(String phoneNumber);

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.timestamp < :cutoff")
    void deleteExpired(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT o FROM Otp o ORDER BY o.mobile, o.timestamp DESC")
    List<Otp> findAllOrderByMobileAndTimestampDesc();

}

