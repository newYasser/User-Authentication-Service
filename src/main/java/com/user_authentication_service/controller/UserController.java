package com.user_authentication_service.controller;

import com.user_authentication_service.dto.Request;
import com.user_authentication_service.dto.Response;
import com.user_authentication_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api_prefix}auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("signup")
    public ResponseEntity<Response> signup(@RequestBody Request request){
        return userService.signup(request);
    }


}
