package com.sampleLdap.auth;

import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class Utils {

    public int buildCn(){
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder returnValue = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            String NUMBERS = "0123456789";
            returnValue.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return Integer.parseInt(returnValue.toString());
    }
}
