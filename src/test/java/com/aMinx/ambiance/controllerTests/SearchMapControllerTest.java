package com.aMinx.ambiance.controllerTests;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.UserPlacesTagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import com.aMinx.ambiance.controllers.SearchMapController;
import com.aMinx.ambiance.controllers.service.SearchMapService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;



public class SearchMapControllerTest {

    @InjectMocks
    SearchMapController searchMapController;

    @Mock
    SearchMapService searchMapService;

    @Mock
    UtilitiesService utilitiesService;

    @Mock
    UserPlacesTagDTO userPlacesTagDTO;

    @Mock
    List <UserPlaces> userPlacesList;

    @Mock
    UserPlaces userPlaces;

    @Mock
    Errors errors;

    @Mock
    User user;

    @Mock
    Place place;

    @Mock
    Tag tag;

    @Mock
    ExtendedModelMap model;



    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateSearchMap(){
        assertThat("places/searchMap", is(searchMapController.createSearchMap( model)));
    }

    @Test
    public void testCheckUserPlacesAddPlace_InUserPlaces(){
        String name= "Lona's", numLat ="30.568", numLng="90.5436", url="www.lonaslileats", phone="314-805-7764";
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(searchMapService.isInUserPlaces(user, name, numLat, numLng)).thenReturn(true);
        assertThat("The place that you have selected has already been added to your list.", is(searchMapController.checkUserPlacesAddPlace(name, numLat, numLng, url, phone)));
    }

    @Test
    public void testCheckUserPlacesAddPlace(){
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(searchMapService.isInUserPlaces(user, place.getName(), place.getNumLat(), place.getNumLng())).thenReturn(false);
        assertThat("You have successfully added to your list! Would you Like to add some tags?", is(searchMapController.checkUserPlacesAddPlace(place.getName(), place.getNumLat(), place.getNumLng(), place.getUrl(), place.getPhone())));
    }

    @Test
    public void testCreateAddTagDialogueBox_NotInUserPlaces(){
        String name= "Lona's", numLat ="30.568", numLng="90.5436", url="www.lonaslileats", phone="314-805-7764";
        List<UserPlaces> upList = new ArrayList<>();
        upList.add(userPlaces);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(upList);
        assertThat("searchMap", is(searchMapController.createAddTagDialogueBox(name, numLat, numLng, url, phone, new ExtendedModelMap())));
    }

    @Test
    public void testCreateAddTagDialogueBox(){
        String name= "Lona's", numLat ="30.568", numLng="90.5436", url="www.lonaslileats", phone="314-805-7764";
        List<UserPlaces> upList = new ArrayList<>();
        UserPlaces up = new UserPlaces();
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        up.setPlace(aPlace);
        up.setUser(user);
        up.getTags().add(tag);
        upList.add(up);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(upList);
        when(utilitiesService.comparePlaceAndPlaceData(aPlace, numLat, numLng)).thenReturn(true);
        assertThat("addTags/"+ aPlace.getId(), is(searchMapController.createAddTagDialogueBox(name, numLat, numLng, url, phone, new ExtendedModelMap())));
    }

    @Test
    public void testCreateAddTags_PlaceNullName(){
        String name= "", numLat ="30.568", numLng="90.5436", url="www.lonaslileats", phone="314-805-7764";
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        Integer placeIdInt = aPlace.getId();
        when(searchMapService.findPlace(placeIdInt)).thenReturn(aPlace);
        assertThat("searchMap", is(searchMapController.createAddTags(Integer.toString(aPlace.getId()), model)));
    }

    @Test
    public void testCreateAddTags(){
        String name= "Lona's", numLat ="30.568", numLng="90.5436", url="www.lonaslileats", phone="314-805-7764";
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        Integer placeIdInt = aPlace.getId();
        when(searchMapService.findPlace(placeIdInt)).thenReturn(aPlace);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(userPlacesList);
        when(searchMapService.findUserPlacesByPlace(userPlacesList, place)).thenReturn(userPlaces);
        assertThat("places/addTags", is(searchMapController.createAddTags(Integer.toString(aPlace.getId()), model)));
    }

    @Test
    public void testProcessAddTag_HasErrors(){
        int [] tagIds = {1,2,3,4,5};
        when(errors.hasErrors()).thenReturn(true);
        assertThat("places/addTags", is(searchMapController.processAddTags(userPlacesTagDTO, tagIds, errors, model)));
    }

    @Test
    public void testProcessAddTag_NullArray(){
        assertThat("places/addTags", is(searchMapController.processAddTags(userPlacesTagDTO, null, errors, model)));
    }

    @Test
    public void testProcessAddTag(){
        int [] tagIds = {1,2,3,4,5};
        assertThat("redirect:/user/dashboard", is(searchMapController.processAddTags(userPlacesTagDTO, tagIds, errors, model)));
    }

    @Test
    public void testCreateAddTagWithPlaceSelected(){
        String userPlacesId = Integer.toString(userPlaces.getId());
        assertThat("places/createTagsWithPlaceSelected", is(searchMapController.createAddNewTagWithPlaceSelected(userPlacesId, model)));
    }

    @Test
    public void testProcessAddTagWithPlaceSelected_HasErrors(){
        String userPlacesId = Integer.toString(userPlaces.getId());
        when(errors.hasErrors()).thenReturn(true);
        assertThat("places/createTagsWithPlaceSelected", is(searchMapController.processAddNewTagWithPlaceSelected(tag, errors, userPlacesId, model)));
    }

    @Test
    public void testProcessAddTagWithPlaceSelected_NullId() {
        assertThat("redirect:/places/searchMap", is(searchMapController.processAddNewTagWithPlaceSelected(tag, errors, null, model)));
    }

    @Test
    public void testProcessTagUserPlacesData(){
        String userPlacesId = Integer.toString(userPlaces.getId());
        assertThat("redirect:/user/dashboard", is(searchMapController.processAddNewTagWithPlaceSelected(tag, errors, userPlacesId, model)));
    }

}
