package com.sampleLdap.auth;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.beans.factory.annotation.Value;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().
                antMatchers(HttpMethod.POST, "/user/add").
                permitAll().
                antMatchers(HttpMethod.GET, "/user/fetch").
                permitAll().
                antMatchers(HttpMethod.POST, "/user/signin").
                permitAll().
                antMatchers(HttpMethod.PATCH, "/user/update").
                permitAll().
                antMatchers(HttpMethod.DELETE, "/user/delete").
                permitAll().
                anyRequest().authenticated().and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                exceptionHandling();
    }
}
