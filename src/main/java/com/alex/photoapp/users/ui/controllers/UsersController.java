package com.alex.photoapp.users.ui.controllers;

import com.alex.photoapp.users.service.UsersService;
import com.alex.photoapp.users.shared.UserDto;
import com.alex.photoapp.users.ui.model.CreateUserRequestModel;
import com.alex.photoapp.users.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static Logger logger = LoggerFactory.getLogger(UsersController.class);

    // Add enviroment variable for zuul
    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;

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

    /**
     * Create a new user with encoded password.
     * Always return ResponseEntity with the status and also a body if we need.
     * Adding support to return JSON (was by default) or XML
     * @param userDetails
     * @return
     */
    @PostMapping(
            produces = {
                    MediaType.APPLICATION_XML_VALUE,    // XML is first if 2 are allowed
                    MediaType.APPLICATION_JSON_VALUE
            },
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    //@PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
    //public ResponseEntity createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
    //public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        logger.info("Receved createUser request: {}", userDetails);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        // copy data using mapper and return to the encrypted user
        UserDto createUser = usersService.createUser(userDto);
        CreateUserResponseModel returnValue = modelMapper.map(createUser, CreateUserResponseModel.class);

        //return new ResponseEntity(HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue); // include body into http response
    }
}
