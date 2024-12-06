package com.user_authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Response {
    private int statusCode;
    private String responseMessage;
    private UserInfo userInfo;
    private OTPResponse otpResponse;
}
