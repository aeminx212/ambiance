package com.aMinx.ambiance.models.dto;

import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.UserPlaces;

import java.util.ArrayList;
import java.util.List;

public class UserPlacesTagDTO {


    private UserPlaces userPlaces;


    private List<Tag> tagsToAdd = new ArrayList<Tag>();

    public UserPlacesTagDTO() {
    }

    public UserPlaces getUserPlaces() {
        return userPlaces;
    }

    public void setUserPlaces(UserPlaces userPlaces) {
        this.userPlaces = userPlaces;
    }

    public List <Tag> getTagsToAdd() {
        return tagsToAdd;
    }

    public void setTagsToAdd(List<Tag> tagsToAdd) {
        this.tagsToAdd = tagsToAdd;
    }
}


