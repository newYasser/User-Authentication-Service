package com.user_authentication_service.utils;

import com.user_authentication_service.dto.Response;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    private ResponseUtils(){
        throw  new UnsupportedOperationException("Utility class");
    }

    public static ResponseEntity<Response> buildSuccessResponse(String message, Object data){
        return ResponseEntity.ok(Response.builder()
                        .statusCode(200)
                        .responseMessage(message)
                        .data(data)
                        .build());
    }

    public static ResponseEntity<Response> buildErrorResponse(String message, int statusCode){
        return ResponseEntity.status(statusCode).body(Response.builder()
                        .statusCode(statusCode)
                        .responseMessage(message)
                        .build());
    }
}
