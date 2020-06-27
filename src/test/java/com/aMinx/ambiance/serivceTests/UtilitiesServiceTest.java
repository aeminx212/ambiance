package com.aMinx.ambiance.serivceTests;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.data.UserDAO;
import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilitiesServiceTest {


    @InjectMocks
    UtilitiesService utilitiesService;

    @Mock
    HttpSession session;

    @Mock
    HttpServletRequest request;

    @Mock
    User user;

    @Mock
    UserDAO userDAO;

    @Mock
    UserPlacesDAO userPlacesDAO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserFromSession_UserIdNull(){
        Integer userId = null;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        assertThat(null, is(utilitiesService.getUserFromSession()));
    }

    @Test
    public void testGetUserFromSession_UserNull(){
        Integer userId = 5;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(5);
        assertThat(null, is(utilitiesService.getUserFromSession()));
    }

    @Test
    public void testGetUserFromSession(){
        Integer userId = 5;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(5);
        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        assertThat(user, is(utilitiesService.getUserFromSession()));
    }

    @Test
    public void testFindUserPlaces_ThrowsNullPointerException(){
        try{
        when(userPlacesDAO.findAll()).thenThrow(new NullPointerException());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        List<UserPlaces> upsList = utilitiesService.findUserPlaces(user);
        Assertions.assertNotNull(upsList);
        Assertions.assertEquals(0, upsList.size());
    }

    @Test
    public void testFindUserPlaces(){
        List<UserPlaces> upsList = new ArrayList<>();
        assertThat(upsList, is(utilitiesService.findUserPlaces(user)));
    }


    @Test
    public void testComparePlaceAndPlaceData_True(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        assertThat(true, is(utilitiesService.comparePlaceAndPlaceData(aPlace, numLat, numLng)));
    }

    @Test
    public void testComparePlaceAndPlaceData_False(){
        String name = "Lona's", numLat = "34.56", numLng = "34.654", url= "www.cake.com", phone = "314-665-7818";
        Place aPlace = new Place(name,numLat, numLng, url, phone);
        String mockNumLat = "654.4", mockNumLng = "4948";
        assertThat(false, is(utilitiesService.comparePlaceAndPlaceData(aPlace, mockNumLat, mockNumLng)));
    }

    @Test
    public void testGetUserTagsFromSession(){
        UtilitiesService utilitiesServiceMock = mock(UtilitiesService.class);
        List <Tag> tags = new ArrayList<>();
        assertThat(tags, is(utilitiesServiceMock.getUserTagsFromSession()));
    }

}
