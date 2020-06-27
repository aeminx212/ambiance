package com.aMinx.ambiance.models;


import javax.persistence.Entity;

@Entity
public class Place extends AbstractEntity {


    private String name;

    private String numLat;

    private String numLng;

    private String url;

    private String phone;

    public Place(String name, String numLat, String numLng, String url, String phone) {
        this.name = name;
        this.numLat = numLat;
        this.numLng = numLng;
        this.url = url;
        this.phone = phone;
    }

    public Place() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumLat() {
        return numLat;
    }

    public String getNumLng() {
        return numLng;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }


    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                '}';
    }
}
