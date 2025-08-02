package com.backend.amazon.service;

import com.backend.amazon.dto.OTPRequest;
import com.backend.amazon.dto.OTPResponse;
import com.backend.amazon.dto.OTPVerificationRequest;
import com.backend.amazon.model.OTP;
import com.backend.amazon.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;

    @Autowired(required = false)
    private EmailService emailService;

    public OTPResponse generateOTP(OTPRequest request) {
        // Generate new OTP
        String otpCode = generateOTPCode();
        OTP otp = new OTP(request.getEmail(), otpCode);
        OTP savedOtp = otpRepository.save(otp);

        // Send OTP via email (if email service is available)
        try {
            if (emailService != null) {
                emailService.sendOTPEmail(request.getEmail(), otpCode, savedOtp.getId());
                return new OTPResponse("OTP sent to your email successfully",
                        savedOtp.getId(), request.getEmail(), true);
            } else {
                // For testing without email configuration
                return new OTPResponse("OTP generated successfully. OTP: " + otpCode,
                        savedOtp.getId(), request.getEmail(), true);
            }
        } catch (Exception e) {
            // If email fails, return OTP in response for testing
            return new OTPResponse("OTP generated successfully. OTP: " + otpCode,
                    savedOtp.getId(), request.getEmail(), true);
        }
    }

    public OTPResponse verifyOTP(OTPVerificationRequest request) {
        // Find valid OTP by ID and code
        Optional<OTP> otpOptional = otpRepository.findByIdAndOtpCodeAndUsedFalseAndExpiresAtAfter(
                request.getOtpId(), request.getOtpCode(), LocalDateTime.now());

        if (otpOptional.isEmpty()) {
            return new OTPResponse("Invalid or expired OTP",
                    null, null, false);
        }

        OTP otp = otpOptional.get();
        otp.setUsed(true);
        otpRepository.save(otp);

        return new OTPResponse("OTP verified successfully",
                otp.getId(), otp.getEmail(), true);
    }

    private String generateOTPCode() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

}