package com.alex.photoapp.users.service;

import com.alex.photoapp.users.data.UserEntity;
import com.alex.photoapp.users.data.UsersRepository;
import com.alex.photoapp.users.shared.UserDto;
import com.alex.photoapp.users.ui.controllers.UsersController;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The method loadUserByUsername comes from UserDetailsService spring framework is called by Spring security framework.
 * @see UserDetailsService that comes fron @see UsersService
 */
@Service
public class UsersServiceImpl implements  UsersService {

    private static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    // use for service auth
    private UsersRepository usersRepository;

    // Encrypt password with encoder. Need to create a bean of this type
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructor base dependencies injection for UsersRepository & BCryptPasswordEncoder
     * @see com.alex.photoapp.users.config.AppConfig that creates bean BCryptPasswordEncoder
     *
     * @param usersRepository
     * @return
     */
    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        // Create a unique userId and encrypt it using Spring Security later
        userDetails.setUserId(UUID.randomUUID().toString());

        // Set real one later when add security
        //userEntity.setEncryptedPassword("test");
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        // use ModelMapper to map the source object into the destination object
        ModelMapper modelMapper = new ModelMapper();

        // map the same fields from source object to destination object.
        // to work perfectly the fields should have the same name and strategy set to strict.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        usersRepository.save(userEntity);

        // create UserDto copy data using mapper and return to the controller
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        return userDto;
        //return null;
    }

    /**
     * This method comes from UserDetailsService spring framework is called by Spring security framework.
     *
     * This is were the Spring framework is trying to authonticate the user
     * and will look for this method and will relay on this method to return
     * user details from user name provided during login.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // add user define method in usersRepository
        UserEntity userEntity = usersRepository.findByEmail(userName);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userName);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }


}
