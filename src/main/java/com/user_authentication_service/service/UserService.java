package com.user_authentication_service.service;

import com.user_authentication_service.dto.LoginRequest;
import com.user_authentication_service.dto.Request;
import com.user_authentication_service.dto.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Response> signup(Request request);
    ResponseEntity<Response>  login(LoginRequest request);
    Response sendOTP();
    Response validateOTP();
    Response resetPassword();
    Response changePassword();


}
