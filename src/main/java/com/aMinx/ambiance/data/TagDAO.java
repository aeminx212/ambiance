package com.aMinx.ambiance.data;

import com.aMinx.ambiance.models.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDAO extends CrudRepository<Tag,Integer> {
}
