package com.user_authentication_service.utils;


import org.springframework.boot.SpringApplicationRunListener;

import java.util.Random;

public class OTPUtils {

    private static final int NUMBER_OF_OTP_DIGITS = 6;
    public static String generateOtp(){
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        int count = 0;
        while(count < NUMBER_OF_OTP_DIGITS){
            otp.append(random.nextInt(10));
            count++;
        }
        return otp.toString();
    }
}
