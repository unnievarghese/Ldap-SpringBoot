package com.sampleLdap.auth;

import lombok.Data;

@Data
public class SignInRequestModel {

    private String uid;
    private String userPassword;
}
