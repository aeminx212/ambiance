package com.aMinx.ambiance.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag extends AbstractEntity{

    @Size(min= 1, max = 40)
    @NotBlank
    @NotNull
    private String name;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "tags")
    private final List<UserPlaces> userPlaces = new ArrayList<>();

    public Tag(String name){
        this.name = name;
    }

    public Tag(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserPlaces> getTaggedUserPlaces(){
        return userPlaces;
    }
}
