package com.aMinx.ambiance.controllerTests;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.DashboardController;
import com.aMinx.ambiance.controllers.service.DashboardService;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.PlaceTagDTO;
import com.aMinx.ambiance.models.dto.UserPlacesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class DashboardControllerTest {

    @InjectMocks
    DashboardController dashboardController;

    @Mock
    UtilitiesService utilitiesService;

    @Mock
    ExtendedModelMap model;

    @Mock
    DashboardService dashboardService;

    @Mock
    User user;

    @Mock
    UserPlaces userPlaces;

    @Mock
    Place place;

    @Mock
    List<Tag> tags;

    @Mock
    Tag tag;

    @Mock
    List <UserPlaces> userPlacesList;

    @Mock
    List <PlaceTagDTO> placeTagDTOList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateDashboard(){
        when(utilitiesService.getUserTagsFromSession()).thenReturn(tags);
        assertThat("user/dashboard", is(dashboardController.createDashboard(model)));
    }

    @Test
    public void testDisplayMarkers(){
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(userPlacesList);
        when(dashboardService.transferDataFromUserPlacesListToListOfDTOs(userPlacesList)).thenReturn(placeTagDTOList);
        assertThat(placeTagDTOList, is(dashboardController.displayMarkers()));
    }

    @Test
    public void testDisplayFilteredMarkers(){
        String tagId = Integer.toString(tag.getId());
        List<Place> placesToRemoveFromMap = new ArrayList<>();
        placesToRemoveFromMap.add(place);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(userPlacesList);
        when(dashboardService.filterTags(tagId,userPlacesList)).thenReturn(placesToRemoveFromMap);
        assertThat(placesToRemoveFromMap, is(dashboardController.displayFilteredMarkers(tagId)));
    }

    @Test
    public void testCreateDeletePlace_False(){
        String latParam ="55.6";
        String lngParam = "66.76";
        List<UserPlaces> userPlacesList = new ArrayList<>();
        UserPlaces up = new UserPlaces();
        Place place = new Place("Lona's", "23.23", "45.45", "www.lonas.com", "314-987-6767" );
        up.setPlace(place);
        up.setUser(user);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.findUserPlaces(user)).thenReturn(userPlacesList);
        assertThat("user/deletePlace",is(dashboardController.createDeletePlace(latParam, lngParam, model)));
    }

    @Test
    public void testCreateDeletePlace(){
        String latParam ="55.6";
        String lngParam = "66.76";
        List<UserPlaces> userPlacesList = new ArrayList<>();
        UserPlaces up = new UserPlaces();
        Place place = new Place("Lona's", latParam, lngParam, "www.lonas.com", "314-987-6767" );
        up.setPlace(place);
        up.setUser(user);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.findUserPlaces(user)).thenReturn(userPlacesList);
        assertThat("user/deletePlace",is(dashboardController.createDeletePlace(latParam, lngParam, model)));
    }

    @Test
    public void testProcessDeletePlace(){
        UserPlacesDTO userPlacesDTO = new UserPlacesDTO();
        UserPlaces up = new UserPlaces();
        userPlacesDTO.setUserPlaces(up);
        userPlacesDTO.getUserPlaces().setPlace(place);
        userPlacesDTO.getUserPlaces().setUser(user);
        assertThat("user/dashboard",is(dashboardController.processDeletePlace(userPlacesDTO, model)));
    }

}
