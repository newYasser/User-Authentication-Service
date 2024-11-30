package com.user_authentication_service.service.impl;


import com.user_authentication_service.dto.*;
import com.user_authentication_service.entity.OTP;
import com.user_authentication_service.repository.OTPRepository;
import com.user_authentication_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
                .body("This OTP to activate you account. It will expire in 15 minutes "+  otp)
                .build());

        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Message Sent")
                .build());
    }


    public ResponseEntity<Response> validOtp(OTPValidationRequest request){

        Optional<OTP> otpOptional = otpRepository.findByEmail(request.getEmail());
        if(otpOptional.isEmpty()){
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseMessage("No OTP have sent yet")
                    .statusCode(400)
                    .build());
        }

        OTP otp = otpOptional.get();

        if(otp.getExpiredAt().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseMessage("Expired OTP")
                    .statusCode(400)
                    .build());
        }

        if(!otp.getOtp().equals(request.getOtp())){
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(400)
                    .responseMessage("Invalid OTP")
                    .build());
        }

        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .responseMessage("Valid OTP")
                .otpResponse(OTPResponse.builder()
                        .isValid(true)
                        .build())
                .build());
    }
}
