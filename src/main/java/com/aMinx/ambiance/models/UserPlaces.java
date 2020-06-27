package com.aMinx.ambiance.models;

import com.aMinx.ambiance.data.UserPlacesDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class UserPlaces extends AbstractEntity{

    @OneToOne
    private User user;

    @OneToOne
    private Place place;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    public UserPlaces() {
    }

    public UserPlaces(User user, Place place) {
        this.user = user;
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Tag> getTags() {
        return tags;
    }

}
