package com.user_authentication_service.controller;

import com.user_authentication_service.dto.OTPRequest;
import com.user_authentication_service.dto.Response;
import com.user_authentication_service.service.impl.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${prefix_api}otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/send")
    public ResponseEntity<Response> sendOTP(@RequestBody OTPRequest request){
        return otpService.sendOTP(request);
    }

}
