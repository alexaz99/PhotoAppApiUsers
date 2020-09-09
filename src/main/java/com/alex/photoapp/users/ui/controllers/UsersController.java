package com.alex.photoapp.users.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    /**
     * http://alex-desktop:{$port}/users/status/check
     * {$port} dynamic binding from Eureka dashboard
     */
    @GetMapping("/status/check")
    public String status() {
        return "working";
    }
}
