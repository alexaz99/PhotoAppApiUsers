package com.alex.photoapp.users.data;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA interface that extends CrudRepository
 *
 * First what we save, second is id of user entity to save. It's Long
 * @see UserEntity
 *      private long id
 */
public interface UsersRepository extends CrudRepository<UserEntity, Long> {

}
