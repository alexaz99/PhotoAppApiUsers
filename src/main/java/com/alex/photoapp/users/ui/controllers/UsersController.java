package com.alex.photoapp.users.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
