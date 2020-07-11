package com.aMinx.ambiance.serivceTests;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.service.SearchMapService;
import com.aMinx.ambiance.data.PlaceDAO;
import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.UserPlacesTagDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SearchMapServiceTest {

    @InjectMocks
    SearchMapService searchMapService;

    @Mock
    UtilitiesService utilitiesService;

    @Mock
    UserPlaces userPlaces;

    @Mock
    UserPlacesTagDTO userPlacesTagDTO;

    @Mock
    Place place;

    @Mock
    PlaceDAO placeDAO;

    @Mock
    UserPlacesDAO userPlacesDAO;

    @Mock
    List <UserPlaces> userPlacesList;

    @Mock
    User user;

    @Mock
    Tag tag;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsInUserPlaces_ThrowsNullPointerException(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        try{
            when(utilitiesService.findUserPlaces(user)).thenThrow(new NullPointerException());
        } catch (NullPointerException e) {
           // e.printStackTrace();
        }
        boolean inUserPlaces = searchMapService.isInUserPlaces(user, name, numLat, numLng);
        Assertions.assertNotNull(inUserPlaces);
        Assertions.assertEquals(false, inUserPlaces);
    }

    @Test
    public void testIsInUserPlaces_False(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        List<UserPlaces> upsList = new ArrayList<>();
        UserPlaces ups = new UserPlaces();
        ups.setPlace(place);
        ups.setUser(user);
        ups.getTags().add(tag);
        upsList.add(ups);
        when(utilitiesService.findUserPlaces(user)).thenReturn(upsList);
        UserPlaces each = ups;
        when(utilitiesService.comparePlaceAndPlaceData(each.getPlace(), numLat, numLng)).thenReturn(false);
        assertThat(false, is(searchMapService.isInUserPlaces(user, name, numLat, numLng)));
    }

    @Test
    public void testIsInUserPlaces(){
        String name = place.getName(), numLat = place.getNumLat(), numLng = place.getNumLng(), url= place.getUrl(), phone = place.getPhone();
        List<UserPlaces> upsList = new ArrayList<>();
        upsList.add(userPlaces);
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        userPlaces.setPlace(aPlace);
        userPlaces.setUser(user);
        userPlaces.getTags().add(tag);
        when(utilitiesService.findUserPlaces(user)).thenReturn(upsList);
        UserPlaces each = userPlaces;
        when(utilitiesService.comparePlaceAndPlaceData(each.getPlace(), numLat, numLng)).thenReturn(true);
        assertThat(true, is(searchMapService.isInUserPlaces(user, name, numLat, numLng)));
    }

    @Test
    public void testAddPlaceToUserPlaces_ThrowsNullPointerException(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        try{
            when(placeDAO.findAll()).thenThrow(new NullPointerException());
        } catch (NullPointerException e) {
           // e.printStackTrace();
        }
        Place aPlace = searchMapService.addPlaceToUserPlaces(user, name, numLat, numLng, url, phone);
        Assertions.assertNotNull(aPlace);
        Assertions.assertEquals("Lona's", aPlace.getName());
    }

    @Test
    public void testAddPlaceToUserPlaces(){
        String name = place.getName(), numLat = place.getNumLat(), numLng = place.getNumLng(), url= place.getUrl(), phone = place.getPhone();
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        assertThat(aPlace, is(searchMapService.addPlaceToUserPlaces(user, name, numLat, numLng, url, phone)));
    }

    @Test
    public void testFindUserPlacesByPlace_Null(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        List<UserPlaces> upsList = new ArrayList<>();
        UserPlaces ups = new UserPlaces();
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        Place place = new Place("bob's", "34.65", "433.3", "www.bobs.com", "454-655-8767");
        ups.setPlace(aPlace);
        ups.setUser(user);
        upsList.add(ups);
        assertThat(null, is(searchMapService.findUserPlacesByPlace(upsList, place)));
    }

    @Test
    public void testFindUserPlacesByPlace(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        List<UserPlaces> upsList = new ArrayList<>();
        UserPlaces ups = new UserPlaces();
        Place aPlace = new Place(name, numLat, numLng, url, phone);
        Place place = aPlace;
        ups.setPlace(aPlace);
        ups.setUser(user);
        upsList.add(ups);
        assertThat(ups, is(searchMapService.findUserPlacesByPlace(upsList, aPlace)));
    }

    @Test
    public void testComparePlaces_True(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        Place bPlace = new Place(name,numLat, numLng, url, phone);
        assertThat(true, is(searchMapService.comparePlaces(aPlace, bPlace)));
    }

    @Test
    public void testComparePlaces_False(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        assertThat(false, is(searchMapService.comparePlaces(aPlace, place)));
    }

    @Test
    public void testFindPlace_NullPlace(){
        Place aPlace = new Place();
        Integer placeId = 5;
        when(placeDAO.findById(placeId)).thenReturn(null);
        assertThat(aPlace, is(searchMapService.findPlace(placeId)));
    }

    @Test
    public void testFindPlace(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        Integer placeId = aPlace.getId();
        when(placeDAO.findById(placeId)).thenReturn(Optional.of(aPlace));
        assertThat(aPlace, is(searchMapService.findPlace(placeId)));
    }

    @Test
    public void testCreateAPlace(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        assertThat(aPlace, is(searchMapService.createNewPlace(name, numLat, numLng, url, phone)));
    }

    @Test
    public void testCreateNewUserPlaces(){
        SearchMapService searchMapServiceMock = mock(SearchMapService.class);
        doAnswer(invocation ->{
            User aUser = invocation.getArgument(0);
            Place aPlace = invocation.getArgument(1);
            assertEquals(user, aUser);
            assertEquals(place, aPlace);
            return null;
        }).when(searchMapServiceMock).createNewUserPlaces(user, place);
        searchMapServiceMock.createNewUserPlaces(user, place);
    }

    @Test
    public void testSetTagsInUserPlacesTagDTO(){
        int [] tagIds = {1,2,3,4};
        SearchMapService searchMapServiceMock = mock(SearchMapService.class);
        doAnswer(invocation ->{
            UserPlacesTagDTO userPlacesTagDTOMock = invocation.getArgument(0);
            int [] tagIdsMock = invocation.getArgument(1);
            assertEquals(userPlacesTagDTO, userPlacesTagDTOMock);
            assertEquals(tagIds, tagIdsMock);
            return null;
        }).when(searchMapServiceMock).setTagsInUserPlacesTagDTO(userPlacesTagDTO, tagIds);
        searchMapServiceMock.setTagsInUserPlacesTagDTO(userPlacesTagDTO, tagIds);
    }

    @Test
    public void testSaveTagsToSelectedUserPlaces(){
        SearchMapService searchMapServiceMock = mock(SearchMapService.class);
        doAnswer(invocation ->{
            UserPlacesTagDTO userPlacesTagDTOMock = invocation.getArgument(0);
            assertEquals(userPlacesTagDTO, userPlacesTagDTOMock);
            return null;
        }).when(searchMapServiceMock).saveTagsToSelectedUserPlaces(userPlacesTagDTO);
        searchMapServiceMock.saveTagsToSelectedUserPlaces(userPlacesTagDTO);
    }

    @Test
    public void testSaveTagToUserPlacesByTagId(){
        String userPlacesId = "5";
        SearchMapService searchMapServiceMock = mock(SearchMapService.class);
        doAnswer(invocation ->{
            Tag tagMock = invocation.getArgument(0);
            String userPlacesIdMock = invocation.getArgument(1);
            assertEquals(tag, tagMock);
            assertEquals(userPlacesId, userPlacesIdMock);
            return null;
        }).when(searchMapServiceMock).saveTagToUserPlacesByTagId(tag, userPlacesId);
        searchMapServiceMock.saveTagToUserPlacesByTagId(tag, userPlacesId);
    }

    @Test
    public void testSaveTag(){
        SearchMapService searchMapServiceMock = mock(SearchMapService.class);
        doAnswer(invocation ->{
            Tag tagMock = invocation.getArgument(0);
            assertEquals(tag, tagMock);
            return null;
        }).when(searchMapServiceMock).saveTag(tag);
        searchMapServiceMock.saveTag(tag);
    }

}
