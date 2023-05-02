package com.sampleLdap.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;

@Configuration
public class Config {

    @Value("${spring.ldap.urls}")
    private String ldapUrls;

    @Value("${spring.ldap.base}")
    private String ldapBase;

    @Value("${spring.ldap.password}")
    private String ldapPassword;

    @Value("${initial.context.factory}")
    private String factory;

    public DirContext makeConnection() {

        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, ldapUrls);
        env.put(Context.SECURITY_PRINCIPAL, ldapBase);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);

        try {
            return new InitialDirContext(env);
        }
        catch(AuthenticationException ex) {
            System.out.println(ex.getMessage());
        }
        catch(NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
}