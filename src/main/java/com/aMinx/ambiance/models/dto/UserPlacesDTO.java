package com.aMinx.ambiance.models.dto;

import com.aMinx.ambiance.models.UserPlaces;

import javax.validation.constraints.NotNull;

public class UserPlacesDTO {


    private UserPlaces userPlaces;

    public UserPlacesDTO() {
    }

    public UserPlaces getUserPlaces() {
        return userPlaces;
    }

    public void setUserPlaces(UserPlaces userPlaces) {
        this.userPlaces = userPlaces;
    }
}
