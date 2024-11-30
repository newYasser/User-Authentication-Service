package com.user_authentication_service.service.impl;

import com.user_authentication_service.dto.LoginRequest;
import com.user_authentication_service.dto.Request;
import com.user_authentication_service.dto.Response;
import com.user_authentication_service.dto.UserInfo;
import com.user_authentication_service.entity.User;
import com.user_authentication_service.repository.UserRepository;
import com.user_authentication_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final int BAD_REQUEST_STATUS_NUMBER = 400;
    private final int OK_REQUEST_STATUS_NUMBER = 200;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper userModelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response> signup(Request request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Response.builder()
                    .statusCode(BAD_REQUEST_STATUS_NUMBER)
                    .responseMessage("This user is already signed up")
                    .build());
        }

        User user =  userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build());

        return ResponseEntity.ok().body(Response.builder()
                .statusCode(OK_REQUEST_STATUS_NUMBER)
                .responseMessage("User signed up successfully")
                .userInfo(userModelMapper.map(user, UserInfo.class))
                .build());
    }

    @Override
    public ResponseEntity<Response>  login(LoginRequest request) {
        return null;
    }

    @Override
    public Response sendOTP() {
        return null;
    }

    @Override
    public Response validateOTP() {
        return null;
    }

    @Override
    public Response resetPassword() {
        return null;
    }

    @Override
    public Response changePassword() {
        return null;
    }
}
