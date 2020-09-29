package com.alex.photoapp.users.security;

import com.alex.photoapp.users.service.UsersService;
import com.alex.photoapp.users.shared.UserDto;
import com.alex.photoapp.users.ui.model.LoginUserRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Extends springframework.security.web.authentication class
 * When this filter is triggered during each login call
 * this class will attempt to perform user Authentication
 *
 * The idea is to a read user / password within this class and call an Authentication manager.
 * We should override attemptAuthentication method which will be called to check Authentication.
 *
 * Important: We need to register this class AuthenticationFilter with HttpSecurity in
 * @see WebSecurity class
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersService usersService;
    private Environment environment;

    public AuthenticationFilter(UsersService usersService,
                                Environment environment,
                                AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * This method will be triggered when a user called
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginUserRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginUserRequestModel.class);

            // getAuthenticationManager().authenticate comes from spring security
            // if this call is successful, Spring framework will call another method which we will override it too.
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
            // comment handling those exceptions because they are the children of IOException
/*        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If Authentication is successful in attemptAuthentication call, this method is called by a Spring framework.
     *
     * This method takes user details and generates GWT token and adds this GWT token to response header and
     * returns it back with HTTP response. And the client will use this GWT token and use it in subsequent
     * requests to our application as an authorization  header!
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // take a user detail and generate a gwt token
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDetails = usersService.getUserDetailsByEmail(userName);

        // generate GWT token
        // need to add jar to pom.xml
        // Also add token.expiration_time & token.secret to application properties file
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        // add this generated token and set in response header token and userId
        // if user/pass authentication is correct
        response.addHeader("token", token);

        response.addHeader("userId", userDetails.getUserId());

    }
}
