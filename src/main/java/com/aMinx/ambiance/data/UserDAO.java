package com.aMinx.ambiance.data;

import com.aMinx.ambiance.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}