package com.alex.photoapp.users.data;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA interface that extends CrudRepository
 *
 * First what we save, second is id of user entity to save. It's Long
 * @see UserEntity
 *      private long id
 *
 * If we want to add user define methods there is a convention to do this in JPA.
 *  findBy    start with startBy if select a record from DB and provide column names by which to search
 *  deleteXXX start with delete
 *  updateXX  start with update create a record to update the record in DB
 */
public interface UsersRepository extends CrudRepository<UserEntity, Long> {

    /**
     * User define method to find a record by email field
     * email database column name should exists in the table.
     */
    UserEntity findByEmail(String email);
}
