package com.aMinx.ambiance.controllers.service;

import com.aMinx.ambiance.data.PlaceDAO;
import com.aMinx.ambiance.data.TagDAO;
import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.UserPlacesTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class SearchMapService {

    @Autowired
    UtilitiesService utilitiesService;

    @Autowired
    private UserPlacesDAO userPlacesDAO;

    @Autowired
    TagDAO tagDAO;

    @Autowired
    PlaceDAO placeDAO;

    public boolean isInUserPlaces(User user, String name, String numLat, String numLng)throws NullPointerException{
        try {
            List<UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
            for (UserPlaces each : userPlaces) {
                if (utilitiesService.comparePlaceAndPlaceData(each.getPlace(), numLat, numLng)){
                    return true;
                }
            }
        }catch (NullPointerException e){
//            e.printStackTrace();
//            System.out.println("UserPlaces null");
        }
        return false;
    }

    public Place addPlaceToUserPlaces(User user, String name, String numLat, String numLng, String url, String phone) {
        Place place = new Place();
        int count = 0;
//        if(placeDAO.findAll().equals(null)){
//            place = createNewPlace(name, numLat, numLng, url, phone);
//            createNewUserPlaces(user, place);
//        }
        try {
            Iterable<Place> placeList = placeDAO.findAll();
            Iterator<Place> placeIterator = placeList.iterator();

                while (placeIterator.hasNext()) {
                    Place aPlace = placeIterator.next();
                    if (utilitiesService.comparePlaceAndPlaceData(aPlace, numLat, numLng)) {
                        createNewUserPlaces(user, aPlace);
                        place = aPlace;
                        count++;
                        break;
                    }
                }

            /*if the selected place was not a Place or in the users myPlaces,
            then create a Place and add it to the user's myPlaces
             */
            if (count < 1) {
                place = createNewPlace(name, numLat, numLng, url, phone);
                createNewUserPlaces(user, place);
            }
        } catch (NullPointerException e) {
//            e.printStackTrace();
//            System.out.println("Places List is null");
            place = createNewPlace(name, numLat, numLng, url, phone);
            createNewUserPlaces(user, place);
        }
        return place;
    }

    public UserPlaces findUserPlacesByPlace(List<UserPlaces> userPlacesListByUser, Place aPlace) {
        UserPlaces userPlaces = null;
        for (UserPlaces each : userPlacesListByUser) {
            if (comparePlaces(each.getPlace(), aPlace)){
                userPlaces = each;
            }
        }
        return userPlaces;
    }

    public boolean comparePlaces(Place place, Place aPlace){
        if(place.getNumLat().equals(aPlace.getNumLat())&& place.getNumLng().equals(aPlace.getNumLng())) {
            return true;
        }else{
            return false;
        }
    }

    public Place findPlace(Integer placeId){
        Place place = new Place();
        try {
            Optional<Place> result = placeDAO.findById(placeId);
            if(result != null){
                place = result.get();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return place;
    }

    public Place createNewPlace(String name, String numLat, String numLng, String url, String phone){
        Place place = new Place(name, numLat, numLng, url, phone);
        placeDAO.save(place);
        return place;
    }

    public void createNewUserPlaces(User user, Place place){
        UserPlaces ups = new UserPlaces(user,place);
        userPlacesDAO.save(ups);
    }

    public void setTagsInUserPlacesTagDTO(UserPlacesTagDTO userPlacesTagDTO, int [] tagIds){
        //takes the array of user tags and matches it with the tagId
        List <Tag> tagsToSearch = userPlacesTagDTO.getUserPlaces().getUser().getTags();
        List<Tag> tagsToAdd = new ArrayList<>();
        for (int i = 0; i < tagIds.length; i++){
            int taggs = tagIds[i];
            for (Tag each : tagsToSearch){
                if (each.getId() == taggs){
                    tagsToAdd.add(each);
                }
            }

        }
        //collects tags to add
        userPlacesTagDTO.setTagsToAdd(tagsToAdd);
    }

    public void saveTagsToSelectedUserPlaces(UserPlacesTagDTO userPlacesTagDTO){
        for (Tag tag: userPlacesTagDTO.getTagsToAdd()){
            Integer userPlacesInt = userPlacesTagDTO.getUserPlaces().getId();
            String userPlacesId = userPlacesInt.toString();
            saveTagToUserPlacesByTagId(tag, userPlacesId);
        }
    }

    public void saveTagToUserPlacesByTagId(Tag tag, String userPlacesId){
        Optional<UserPlaces> result = userPlacesDAO.findById(Integer.parseInt(userPlacesId));
        UserPlaces userPlaces = result.get();
        userPlaces.getTags().add(tag);
        userPlacesDAO.save(userPlaces);
        User user = userPlaces.getUser();
        tag.setUser(user);
        tagDAO.save(tag);
    }

    public void saveTag(Tag tag){
        tagDAO.save(tag);
    }


}
