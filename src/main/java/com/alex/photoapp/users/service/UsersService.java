package com.alex.photoapp.users.service;

import com.alex.photoapp.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * In This one we use UserDto.
 * Need to implement UserDetailsService from Spring framework
 * to be used in Authentication
 */
public interface UsersService extends UserDetailsService {

    UserDto createUser(UserDto userDetails);
    UserDto getUserDetailsByEmail(String email);

}
