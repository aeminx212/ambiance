package com.aMinx.ambiance.data;

import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDAO extends CrudRepository<Place, Integer> {

}
