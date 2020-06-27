package com.aMinx.ambiance.controllers;


import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.service.TagService;
import com.aMinx.ambiance.models.Place;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping("tags")
public class TagController {

    @Autowired
    UtilitiesService utilitiesService;

    @Autowired
    TagService tagService;


    @GetMapping("manageTags")
    public String createManageTags(Model model){
        List<String> placesToDisplay = new ArrayList<>();
        List<Tag> tags = utilitiesService.getUserTagsFromSession();
        model.addAttribute("title", "Tag Management Options");
        model.addAttribute("tags", tags);
        model.addAttribute("placesToDisplay",placesToDisplay);
        return "tags/manageTags";
    }

    @PostMapping("manageTags")
    public String processManageTags(@RequestParam String tagName, Model model){
        List<String> placesToDisplay = new ArrayList<>();
        User user = utilitiesService.getUserFromSession();
        List <UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
        for(UserPlaces up : userPlaces){
            List <Tag> tagsFromUserPlaces = up.getTags();
            for(Tag tag : tagsFromUserPlaces){
                if(tag.getName().equals(tagName)){
                    placesToDisplay.add(up.getPlace().getName());
                }
            }
        }
        List<Tag> tags = user.getTags();
        model.addAttribute("title", "Tag Management Options");
        model.addAttribute("tags",tags);
        model.addAttribute("oldTagName", tagName);
        model.addAttribute("placesToDisplay",placesToDisplay);
        return "tags/manageTags";
    }

    @GetMapping("addTagsToPlaces")
    public String createAddTagsToPlaces(Model model){
        List <Place> places = new ArrayList();
        User user = utilitiesService.getUserFromSession();
        List<Tag> tags = user.getTags();
        List<UserPlaces> userPlaces = utilitiesService.findUserPlaces(user);
        for(UserPlaces each : userPlaces){
            places.add(each.getPlace());
        }
        model.addAttribute("title", "Add Tags to Places");
        model.addAttribute("tagsTitle", " Your Tags");
        model.addAttribute("placesTitle","Your Places");
        model.addAttribute("tags", tags);
        model.addAttribute("places", places);
        return "tags/addTagsToPlaces";
    }

    @PostMapping ("addTagsToPlaces")
    public String processAddTagToPlaces(@RequestParam (required = false )int [] tagIds,
                                        @RequestParam (required = false )int [] placeIds, Model model){
        String page ="";
        if(tagIds == null || placeIds== null){
            model.addAttribute("title", "Please review your selections.");
            page = "tags/addTagsToPlaces";

        }else{

            User user = utilitiesService.getUserFromSession();
            List<Tag> tags = user.getTags();
            tagService.savedTagsToUserPlaces(user,tagIds, placeIds, tags);
            page = "redirect:../tags/manageTags";
        }
        return page;
    }

    @GetMapping("createTag")
    public String createNewTag(Model model){
        List<Tag> oldTags = utilitiesService.getUserTagsFromSession();
        model.addAttribute("title", "Create A Tag");
        model.addAttribute("oldTags", oldTags);
        model.addAttribute("tag", new Tag());
        return "tags/createTag";
    }

    @PostMapping("createTag")
    public String processNewTag(@ModelAttribute @Valid Tag tag, Errors errors, Model model){
        String page = "";
        if(errors.hasErrors()){
            model.addAttribute("title", "The tag name was not valid.");
            model.addAttribute(tag);
            page= "tags/createTag";
        }
        User user = utilitiesService.getUserFromSession();
        List<Tag> previousTags = user.getTags();
        for(Tag pTag : previousTags) {
            if (pTag.getName().equals(tag.getName())){
                model.addAttribute(tag);
                model.addAttribute("title", "This tag name is already in use.");
                page = "tags/createTag";
            }else {
                tagService.saveTagWithUser(tag,user);
                page = "redirect:/tags/manageTags";
            }
        }
        return page;
    }

    @GetMapping("editTags")
    public String createEditTag(Model model){
        List<Tag> tags = utilitiesService.getUserTagsFromSession();
        model.addAttribute("title", "Select a Tag to Edit");
        model.addAttribute("tags", tags);
        model.addAttribute("tagDTO", new TagDTO());
        return "tags/editTags";
    }

    @PostMapping("editTags")
    public String processEditTag(@RequestParam int tagToEdit, TagDTO tagDTO, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("title", "The last tag name that was entered was invalid. Please try again.");
            return "editTags";
        }
        boolean saved = tagService.updateTag(tagToEdit,tagDTO);
        if(!saved){
            model.addAttribute("title", "Invalid Tag: " + tagToEdit);
        }
        return "redirect:../tags/manageTags";
    }


    @GetMapping("deleteTags")
    public String createDeleteTags(Model model){
        List<Tag> tags = utilitiesService.getUserTagsFromSession();
        model.addAttribute("title", "Delete Tags");
        model.addAttribute("tags", tags);
        return "tags/deleteTags";
    }

    @PostMapping ("deleteTags")
    public String processDeleteTag(@RequestParam (required = false )int [] tagIds){
        if(tagIds != null){
            User user = utilitiesService.getUserFromSession();
            tagService.removeTagsFromUserPlaces(user, tagIds);
        }else{
            return "tags/deleteTags";
        }
        tagService.deleteTag(tagIds);
        return "redirect:../user/dashboard";
    }

}
