package com.sampleLdap.auth;

import lombok.Data;

@Data
public class LdapUser {

    private String givenName;
    private String sn;
    private String userPassword;
    private String uid;
}
