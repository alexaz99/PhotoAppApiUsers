package com.alex.photoapp.users.security;

import com.alex.photoapp.users.service.UsersService;
import com.alex.photoapp.users.service.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

/**
 * This is a configuration class where we define Spring Security Configuration
 * For now permit this HttpSecurity to send to the certain pass.
 *
 * 1. When a user sends an http request to perform a login, a custom filter will be triggered
 *    and a method in AuthenticationFilter.attemptAuthentication will be called.
 *    It will attempt to authenticate user but it needs to know were and how to find the user details.
 *    It should perform authentication.
 * 2. A custom filter should be registered with WebSecurity class
 *    We implement this in configure method and add our custom filter
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecurity.class);

    // need this Environment to read variable setting from application.properties file
    private Environment environment;

    private UsersService usersService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        logger.info("WebSecurity Constructor => Zuul host address: {}", environment.getProperty("gateway.ip"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // disable for now.
        //http.authorizeRequests().antMatchers("/users/**").permitAll(); // this permit all

        // how configure only to except from IP address of Zuul API Gateway
        logger.info("Zuul host address: {}", environment.getProperty("gateway.ip"));
        //http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"));

        // add filter class AuthenticationFilter.java which I want to register
        http.authorizeRequests().antMatchers("/**")
        .hasIpAddress(environment.getProperty("gateway.ip"))
        .and()
        .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable(); // very important. head in order h2-console to work with spring security
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService, environment, authenticationManager());
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }
}
