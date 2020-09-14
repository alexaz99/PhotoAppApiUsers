package com.alex.photoapp.users.ui.controllers;

import com.alex.photoapp.users.ui.model.CreateUserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    // Add enviroment variable for zuul
    @Autowired
    private Environment env;

    /**
     * http://alex-desktop:{$port}/users/status/check
     * {$port} dynamic binding from Eureka dashboard
     *
     * Call this url and check if Zuul is working.
     * http://localhost:8011/users-ws/users/status/check
     */
    @GetMapping("/status/check")
    public String status() {
        return "working on port " + env.getProperty("local.server.port");
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        return "Create user method is called";
    }
}
