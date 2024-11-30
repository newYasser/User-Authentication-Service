package com.user_authentication_service.service.impl;


import com.user_authentication_service.dto.EmailDetails;
import com.user_authentication_service.dto.OTPRequest;
import com.user_authentication_service.dto.Response;
import com.user_authentication_service.entity.OTP;
import com.user_authentication_service.repository.OTPRepository;
import com.user_authentication_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<Response> sendOTP(OTPRequest request){

        String otp = AppUtils.generateOtp();

        otpRepository.save(OTP.builder()
                .email(request.getEmail())
                .otp(otp)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build());

        emailService.sendEmail(EmailDetails.builder()
                .subject("OTP for sign up confirmation")
                .recipient(request.getEmail())
                .body("This is OTP to activate you account. It will expire in 15 minutes")
                .build());

        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Message Sent")
                .build());
    }
}
