package com.aMinx.ambiance.controllers.service;

import com.aMinx.ambiance.data.PlaceDAO;
import com.aMinx.ambiance.data.TagDAO;
import com.aMinx.ambiance.data.UserDAO;
import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class UtilitiesService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserPlacesDAO userPlacesDAO;

    @Autowired
    TagDAO tagDAO;

    @Autowired
    PlaceDAO placeDAO;

    private static final String userSessionKey = "user";

    public static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    public User getUserFromSession() {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userDAO.findById(userId);

        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public List<UserPlaces> findUserPlaces(User user)throws NullPointerException{
        List<UserPlaces> userPlacesByUser = new ArrayList<>();
        try {
            Iterable<UserPlaces> userPlaceList = userPlacesDAO.findAll();
                Iterator<UserPlaces> userPlacesIterator = userPlaceList.iterator();
                while (userPlacesIterator.hasNext()) {
                    UserPlaces aUserPlace = userPlacesIterator.next();
                    if (aUserPlace.getUser().getId() == user.getId()) {
                        userPlacesByUser.add(aUserPlace);
                    }
                }
        }catch(NullPointerException e){
//            e.printStackTrace();
//            System.out.println("UserPlaces is null***");
        }
        return userPlacesByUser;
    }

    public boolean comparePlaceAndPlaceData(Place aPlace, String numLat, String numLng){
        if(aPlace.getNumLat().equals(numLat)&& aPlace.getNumLng().equals(numLng)) {
            return true;
        }else{
            return false;
        }
    }

    public List<Tag> getUserTagsFromSession(){
        User user = getUserFromSession();
        return user.getTags();
    }

}
