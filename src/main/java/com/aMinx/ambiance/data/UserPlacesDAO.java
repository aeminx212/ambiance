package com.aMinx.ambiance.data;

import com.aMinx.ambiance.models.UserPlaces;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPlacesDAO extends CrudRepository<UserPlaces, Integer> {
}
