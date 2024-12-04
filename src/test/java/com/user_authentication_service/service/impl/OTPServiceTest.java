package com.user_authentication_service.service.impl;


import com.user_authentication_service.dto.*;
import com.user_authentication_service.repository.OTPRepository;
import com.user_authentication_service.utils.OTPUtils;
import com.user_authentication_service.entity.OTP;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OTPServiceTest {
    @InjectMocks
    private OTPService otpService;
    @Mock
    private OTPRepository otpRepository;
    @Mock
    private EmailService emailService;

    @Test
    void sendOTP_shouldSendOtpAndSaveToRepository(){

        String email = "test@gmail.com";
        String otpCode = "123456";
        OTPRequest request = OTPRequest.builder().email(email).build();

        try(MockedStatic<OTPUtils> mockedOtpUtils = Mockito.mockStatic(OTPUtils.class)) {


            mockedOtpUtils.when(OTPUtils::generateOtp).thenReturn(otpCode);
            when(otpRepository.findByEmail(email)).thenReturn(Optional.empty());

            ResponseEntity<Response> response = otpService.sendOTP(request);

            ArgumentCaptor<OTP> otpCaptor = ArgumentCaptor.forClass(OTP.class);
            verify(otpRepository).save(otpCaptor.capture());
            OTP savedOtp = otpCaptor.getValue();

            assertThat(savedOtp.getEmail()).isEqualTo(email);
            assertThat(savedOtp.getOtp()).isEqualTo(otpCode);
            assertThat(savedOtp.getExpiredAt()).isAfter(LocalDateTime.now());

            verify(emailService).sendEmail(any(EmailDetails.class));
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isEqualTo(otpCode);
        }

    }

    @Test
    void sendOTP_shouldReplaceExistingOtpIfEmailExists(){

        String email = "test@gmail.com";
        String otpCode = "123456";
        OTPRequest request = OTPRequest.builder().email(email).build();

        try (MockedStatic<OTPUtils> mockedOtpUtils = Mockito.mockStatic(OTPUtils.class)) {
            mockedOtpUtils.when(OTPUtils::generateOtp).thenReturn(otpCode);
            when(otpRepository.findByEmail(email)).thenReturn(Optional.of(new OTP()));

            ResponseEntity<Response> response = otpService.sendOTP(request);

            verify(otpRepository).deleteByEmail(email);
            verify(otpRepository).save(any(OTP.class));

            verify(emailService).sendEmail(any(EmailDetails.class));
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isEqualTo(otpCode);
        }
    }

    @Test
    void validOtp_shouldReturnSuccessForValidOTP(){

        String email = "test@example.com";
        String otpCode = "123456";
        OTP otp = OTP.builder()
                .email(email)
                .otp(otpCode)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();


        OTPValidationRequest request = OTPValidationRequest.builder()
                .email(email)
                .otp(otpCode)
                .build();

        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otp));

        ResponseEntity<Response> response = otpService.validOtp(request);

        assertThat(response.getBody()).isNotNull();
        assertThat(((OTPResponse) response.getBody().getData()).getIsValid()).isTrue();

    }

    @Test
    void validOtp_shouldReturnErrorForInvalidOtp(){
        String email = "test@example.com";
        String otpCode = "123456";
        OTP otp = OTP.builder()
                .email(email)
                .otp(otpCode)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
        OTPValidationRequest request = OTPValidationRequest.builder()
                .email(email)
                .otp("654321")
                .build();

        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otp));

        ResponseEntity<Response> response = otpService.validOtp(request);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatusCode()).isEqualTo(400);
    }

    @Test
    void validOtp_shouldReturnErrorForExpiredOtp() {

        String email = "test@example.com";
        String otpCode = "123456";
        OTP otp = OTP.builder()
                .email(email)
                .otp(otpCode)
                .expiredAt(LocalDateTime.now().minusMinutes(1))
                .build();
        OTPValidationRequest request = OTPValidationRequest.builder()
                .email(email)
                .otp(otpCode)
                .build();

        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otp));

        ResponseEntity<Response> response = otpService.validOtp(request);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatusCode()).isEqualTo(400);
    }




}
