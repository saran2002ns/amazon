package com.backend.amazon.repository;

import com.backend.amazon.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

       

        Optional<OTP> findByIdAndOtpCodeAndUsedFalseAndExpiresAtAfter(
                        Long id, String otpCode, LocalDateTime now);

      
}