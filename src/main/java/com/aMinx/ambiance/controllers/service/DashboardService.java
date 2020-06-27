package com.aMinx.ambiance.controllers.service;

import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.PlaceTagDTO;
import com.aMinx.ambiance.models.dto.UserPlacesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    UserPlacesDAO userPlacesDAO;


    public List<PlaceTagDTO> transferDataFromUserPlacesListToListOfDTOs(List<UserPlaces> userPlacesList ) {
        List<PlaceTagDTO> places = new ArrayList<>();
        for (UserPlaces each : userPlacesList) {
            PlaceTagDTO thisPlaceTag = new PlaceTagDTO();
            List<Integer> tags = new ArrayList<>();
            thisPlaceTag.setName(each.getPlace().getName());
            thisPlaceTag.setNumLat(each.getPlace().getNumLat());
            thisPlaceTag.setNumLng(each.getPlace().getNumLng());
            thisPlaceTag.setUrl(each.getPlace().getUrl());
            thisPlaceTag.setPhone(each.getPlace().getPhone());
            for (Tag tag : each.getTags()) {
                tags.add(tag.getId());
            }
            thisPlaceTag.setPlaceTags(tags);
            places.add(thisPlaceTag);
        }
        return places;
    }

    public List<Place> filterTags(String tagId, List<UserPlaces> userPlaces) {
        Integer filterTag = Integer.parseInt(tagId);
        List<Place> placesToRemoveFromMap = new ArrayList<>();
        for (UserPlaces each : userPlaces) {
            //takes tags from each place
            List<Tag> tagsOnUserPlaces = each.getTags();
            List<Integer> myPlacesTagIds = new ArrayList();
            //puts tagIds from places in list
            for (Tag tag : tagsOnUserPlaces) {
                myPlacesTagIds.add(tag.getId());
            }
            //checks list for filterTag adds place to remove to list
            if (!myPlacesTagIds.contains(filterTag)) {
                placesToRemoveFromMap.add(each.getPlace());
            }
        }
        return placesToRemoveFromMap;
    }

    public void deleteFromUserPlacesDAO(UserPlacesDTO userPlacesDTO){
        userPlacesDAO.delete(userPlacesDTO.getUserPlaces());
    }


}
