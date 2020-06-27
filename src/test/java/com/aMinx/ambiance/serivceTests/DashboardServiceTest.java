package com.aMinx.ambiance.serivceTests;

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

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class DashboardServiceTest {

    @InjectMocks
    DashboardService dashboardService;

    @Mock
    UserPlacesDTO userPlacesDTO;

    @Mock
    Place place;

    @Mock
    User user;

    @Mock
    Tag tag;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransferDataFromUserPlacesListToListOfDTOs(){
        List<UserPlaces> userPlacesList = new ArrayList<>();
        UserPlaces up = new UserPlaces();
        up.setPlace(place);
        up.setUser(user);
        up.getTags().add(tag);
        List<PlaceTagDTO> placeTagDTOList = new ArrayList<>();
        assertThat(placeTagDTOList, is(dashboardService.transferDataFromUserPlacesListToListOfDTOs(userPlacesList)));
    }


    @Test
    public void testFilterTags(){
        List<UserPlaces> userPlacesList = new ArrayList<>();
        String tagId = Integer.toString(tag.getId());
        List<Place> places = new ArrayList<>();
        assertThat(places, is(dashboardService.filterTags(tagId, userPlacesList)));
    }

    @Test
    public void testDeleteFromUserPlacesDAO(){
        DashboardService dashboardServiceMock = mock(DashboardService.class);
        doAnswer(invocation ->{
            UserPlacesDTO aUserPlacesDTO = invocation.getArgument(0);

            assertEquals(userPlacesDTO, aUserPlacesDTO);
            return null;
        }).when(dashboardServiceMock).deleteFromUserPlacesDAO(any(UserPlacesDTO.class));
        dashboardServiceMock.deleteFromUserPlacesDAO(userPlacesDTO);
    }
}
