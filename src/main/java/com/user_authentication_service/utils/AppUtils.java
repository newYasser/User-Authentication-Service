package com.user_authentication_service.utils;


import java.util.Random;

public class AppUtils {

    public static String generateOtp(){
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        int count = 4;
        while(count < 4){
            otp.append(random.nextInt(10));
            count++;
        }
        return otp.toString();
    }
}
