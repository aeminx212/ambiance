package com.aMinx.ambiance.controllers;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.service.SearchMapService;
import com.aMinx.ambiance.controllers.service.TagService;
import com.aMinx.ambiance.data.PlaceDAO;
import com.aMinx.ambiance.data.TagDAO;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.UserPlacesTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
public class SearchMapController {

    @Autowired
    SearchMapService searchMapService;

    @Autowired
    UtilitiesService utilitiesService;

    @GetMapping("places/searchMap")
    public String createSearchMap(Model model) {
        model.addAttribute("title", "Search Map");
        return "places/searchMap";
    }

    @RequestMapping(value="places/searchMap2", method=RequestMethod.POST)
    public @ResponseBody String checkUserPlacesAddPlace(@RequestParam String name, @RequestParam String numLat,
                                                        @RequestParam String numLng, @RequestParam String url,
                                                        @RequestParam String phone){
        String returnStatement ="";
        User user = utilitiesService.getUserFromSession();
        boolean placeInUserPlaces = searchMapService.isInUserPlaces(user, name, numLat, numLng);

        // checks if the place the user wants to add is already in their myPlaces
        if (placeInUserPlaces) {
            returnStatement = "The place that you have selected has already been added to your list.";
            return returnStatement;
        }
        // checks if the place the user wants to add is already in the database & adds it to their list
        Place place = searchMapService.addPlaceToUserPlaces(user, name, numLat, numLng, url, phone);
        returnStatement = "You have successfully added to your list! Would you Like to add some tags?";
        return (returnStatement);
    }

    @GetMapping (value="places/preparingToAddTags")
    public @ResponseBody String createAddTagDialogueBox (@RequestParam String name, @RequestParam String numLat,
                                              @RequestParam String numLng, @RequestParam String url,
                                              @RequestParam String phone, Model model) {
        String returnStatement = "searchMap";
        User user = utilitiesService.getUserFromSession();
        List <UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
        for(UserPlaces up : userPlaces){
            Place aPlace = up.getPlace();
            if(utilitiesService.comparePlaceAndPlaceData(aPlace, numLat, numLng)){
                String placeId = Integer.toString(aPlace.getId());
                returnStatement = "addTags/"+ placeId;
            }
        }
        return returnStatement;
    }

    @GetMapping ("places/addTags/{placeId}")
    public String createAddTags(@PathVariable String placeId, Model model) {
        Integer placeIdInt = Integer.parseInt(placeId);
        Place place = searchMapService.findPlace(placeIdInt);
        if(place.getName() == ""){
            return "searchMap";
        }
        User user = utilitiesService.getUserFromSession();
        List<UserPlaces> userPlacesListByUser = utilitiesService.findUserPlaces(user);
        UserPlaces userPlacesToAddTag = searchMapService.findUserPlacesByPlace(userPlacesListByUser, place);
        UserPlacesTagDTO userPlacesTagDTO = new UserPlacesTagDTO();
        userPlacesTagDTO.setUserPlaces(userPlacesToAddTag);
        List <Tag> tagsToAdd = user.getTags();
        userPlacesTagDTO.setTagsToAdd(tagsToAdd);
        model.addAttribute("userPlacesTagDTO", userPlacesTagDTO);
        model.addAttribute("title", "Add Tags to " + place.getName());

        return "places/addTags";
    }


    @PostMapping ("places/addTags/{placeId}")
    public String processAddTags(@ModelAttribute @Valid UserPlacesTagDTO userPlacesTagDTO,
                                 @RequestParam(value = "tagIds", required = false) int [] tagIds,
                                 Errors errors, Model model) {
        if(errors.hasErrors()){
            model.addAttribute(userPlacesTagDTO);
            return "redirect:/places/addTags/"+userPlacesTagDTO.getUserPlaces().getPlace().getId();
        }
        if (tagIds != null) {
            searchMapService.setTagsInUserPlacesTagDTO(userPlacesTagDTO, tagIds);

        }else{
            return "redirect:/places/addTags/"+userPlacesTagDTO.getUserPlaces().getPlace().getId();
        }
        searchMapService.saveTagsToSelectedUserPlaces(userPlacesTagDTO);
        return "redirect:/user/dashboard";
    }

    @GetMapping (value="places/createTagsWithPlaceSelected")
    public String createAddNewTagWithPlaceSelected(@RequestParam(required= false) String userPlacesId, Model model) {
        Tag tag = new Tag();
        model.addAttribute("tag", tag);
        model.addAttribute("title", "Create a Tag");
        return "places/createTagsWithPlaceSelected";
    }

    @PostMapping (value="places/createTagsWithPlaceSelected")
    public String processAddNewTagWithPlaceSelected(@ModelAttribute @Valid Tag tag, Errors errors,
                                                    @RequestParam (required= false) String userPlacesId, Model model) {
        if(errors.hasErrors()){
            model.addAttribute(tag);
            return "places/createTagsWithPlaceSelected";
        }
        searchMapService.saveTag(tag);
        if (userPlacesId != null) {
            searchMapService.saveTagToUserPlacesByTagId(tag, userPlacesId);
        }else{
            return "redirect:/places/searchMap";
        }
        return "redirect:/user/dashboard";
    }


}
