package com.aMinx.ambiance.models.dto;

import java.util.List;

public class PlaceTagDTO {

    private List<Integer> placeTags;

    private String name;

    private String numLat;

    private String numLng;

    private String url;

    private String phone;

    public PlaceTagDTO() {
    }

    public List<Integer> getPlaceTags() {
        return placeTags;
    }

    public void setPlaceTags(List<Integer> placeTags) {
        this.placeTags = placeTags;
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

    public void setNumLat(String numLat) {
        this.numLat = numLat;
    }

    public String getNumLng() {
        return numLng;
    }

    public void setNumLng(String numLng) {
        this.numLng = numLng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
