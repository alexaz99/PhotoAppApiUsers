package com.alex.photoapp.users.security;

import com.alex.photoapp.users.service.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This is a configuration class where we define Spring Security Configuration
 * For now permit this HttpSecurity to send to the certain pass.
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecurity.class);

    // need this Environment to read variable setting from application.properties file
    private Environment environment;

    @Autowired
    public WebSecurity(Environment environment) {
        this.environment = environment;
        logger.info("WebSecurity Constructor => Zuul host address: {}", environment.getProperty("gateway.ip"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // disable for now.
        //http.authorizeRequests().antMatchers("/users/**").permitAll(); // this permit all

        // how configure only to except from IP address of Zuul API Gateway
        logger.info("Zuul host address: {}", environment.getProperty("gateway.ip"));
        http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"));

        http.headers().frameOptions().disable(); // very important. head in order h2-console to work with spring security
    }
}
