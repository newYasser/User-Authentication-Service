package com.user_authentication_service.service.impl;


import com.user_authentication_service.dto.*;
import com.user_authentication_service.entity.OTP;
import com.user_authentication_service.repository.OTPRepository;
import com.user_authentication_service.utils.OTPUtils;
import com.user_authentication_service.utils.ResponseUtils;
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

        String otp = OTPUtils.generateOtp();

        if(otpRepository.findByEmail(request.getEmail()).isPresent()){
            otpRepository.deleteByEmail(request.getEmail());
        }
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

        return ResponseUtils.buildSuccessResponse("message Sent",otp);
    }


    public ResponseEntity<Response> validOtp(OTPValidationRequest request){

        Optional<OTP> otpOptional = otpRepository.findByEmail(request.getEmail());

        if(otpOptional.isEmpty()){
            return  ResponseUtils.buildErrorResponse("Invalid OTP", 400);
        }

        OTP otp = otpOptional.get();

        if(otp.getExpiredAt().isBefore(LocalDateTime.now())){
            return ResponseUtils.buildErrorResponse("Expired OTP", 400);
        }

        if(!otp.getOtp().equals(request.getOtp())){
            return ResponseUtils.buildErrorResponse("Invalid OTP", 400);
        }

        return ResponseUtils.buildSuccessResponse("Valid OTP",
                OTPResponse.builder()
                        .isValid(true)
                        .build());
    }
}
