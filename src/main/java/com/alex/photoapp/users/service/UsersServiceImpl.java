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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImpl implements  UsersService {

    private static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    private UsersRepository usersRepository;

    // Encrypt password with encoder. Need to create a bean of this type
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructor base dependencies injection
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
}
