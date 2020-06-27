package com.aMinx.ambiance.controllers;


import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.service.DashboardService;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.PlaceTagDTO;
import com.aMinx.ambiance.models.dto.UserPlacesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user/")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    UtilitiesService utilitiesService;


    @GetMapping("dashboard")
    public String createDashboard(Model model){
        List<Tag> existingTags = utilitiesService.getUserTagsFromSession();
        model.addAttribute("title", "Your Places");
        model.addAttribute("existingTags", existingTags);
        return "user/dashboard";
    }

    @RequestMapping(path = "populateMarkers", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List <PlaceTagDTO> displayMarkers(){
        User user = utilitiesService.getUserFromSession();
        List<UserPlaces> userPlacesList = utilitiesService.findUserPlaces(user);
        return dashboardService.transferDataFromUserPlacesListToListOfDTOs(userPlacesList);
    }


    @PostMapping(path ="selectedPlaces", consumes = "text/plain", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List <Place> displayFilteredMarkers (@RequestBody String tagId){
        User user = utilitiesService.getUserFromSession();
        List<UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
        return dashboardService.filterTags(tagId, userPlaces);
    }

    @GetMapping("deletePlace/{latParam}/{lngParam}")
    public String createDeletePlace(@PathVariable String latParam, @PathVariable String lngParam, Model model ){
        User user = utilitiesService.getUserFromSession();
        List<UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
        for (UserPlaces userPlace : userPlaces) {
            if (utilitiesService.comparePlaceAndPlaceData(userPlace.getPlace(), latParam, lngParam)) {
                UserPlacesDTO userPlacesDTO = new UserPlacesDTO();
                userPlacesDTO.setUserPlaces(userPlace);
                model.addAttribute("userPlacesDTO", userPlacesDTO);
            }
        }
        return "user/deletePlace";
    }

    @PostMapping("deletePlace")
    public String processDeletePlace(@ModelAttribute UserPlacesDTO userPlacesDTO, Model model){
        dashboardService.deleteFromUserPlacesDAO(userPlacesDTO);
        String placeName = userPlacesDTO.getUserPlaces().getPlace().getName();
        model.addAttribute("title", placeName +" has been deleted from you places.");
        return "user/dashboard";
    }

}
