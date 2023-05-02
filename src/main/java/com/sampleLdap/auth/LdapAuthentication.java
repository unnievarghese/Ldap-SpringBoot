package com.sampleLdap.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Properties;

@Configuration
public class LdapAuthentication {

    @Value("${initial.context.factory}")
    private String factory;

    @Value("${spring.ldap.urls}")
    private String ldapUrls;


    public Boolean authenticateUser(String cn, String userPassword) throws Exception {
        boolean status = true;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, ldapUrls);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn="+cn+",ou=People,dc=sample,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, userPassword);

        try {
            new InitialDirContext(env);
        }
        catch(Exception ex) {
            status = false;
        }
        return status;
    }

    public void onSuccessFullAuthentication(String cn, HttpServletResponse res, SearchResult searchResult) throws NamingException {
        String token = Jwts.builder().setSubject(cn).
                setExpiration(new Date(System.currentTimeMillis() +864000000)).
                signWith(SignatureAlgorithm.HS512,"jf9i4jgu83nfl0").compact();
        res.addHeader("Authorization","Bearer "+token);
        res.addHeader("roles", searchResult.getAttributes().get("description").get().toString());
    }
}
