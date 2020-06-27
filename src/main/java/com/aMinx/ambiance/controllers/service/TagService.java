package com.aMinx.ambiance.controllers.service;

import com.aMinx.ambiance.data.TagDAO;
import com.aMinx.ambiance.data.UserPlacesDAO;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    private UtilitiesService utilitiesService;

    @Autowired
    private UserPlacesDAO userPlacesDAO;

    @Autowired
    private TagDAO tagDAO;


    public void savedTagsToUserPlaces(User user, int [] tagIds, int [] placeIds, List <Tag> tags){
        List<Tag> tagsToUpdate = new ArrayList<>();
        for(Tag tag: tags){
            for(int id : tagIds) {
                if (tag.getId() == id){
                    tagsToUpdate.add(tag);
                }
            }
        }
        List<UserPlaces> userPlacesToUpdate = utilitiesService.findUserPlaces(user);
        for(UserPlaces each : userPlacesToUpdate){
            for(int placeId: placeIds){
                if(each.getPlace().getId()==placeId){
                    for (Tag addTag: tagsToUpdate){
                        List<Tag> userPlacesTags = each.getTags();
                        if(!(userPlacesTags.contains(addTag))){
                            userPlacesTags.add(addTag);
                        }
                    }
                    userPlacesDAO.save(each);
                }
            }
        }
    }


    public void saveTagWithUser(Tag tag, User user){
        tag.setUser(user);
        tagDAO.save(tag);
    }

// If tag is updated true is returned
    public boolean updateTag(int tagToEdit, TagDTO tagDTO ){
        Optional<Tag> result = tagDAO.findById(tagToEdit);
        if(result.isEmpty()){
            return false;
        }else {
            Tag tag = result.get();
            tag.setName(tagDTO.getName());
            tagDAO.save(tag);
        }
        return true;
    }


    public void removeTagsFromUserPlaces(User user, int [] tagIds){
        List<Tag> tagsToRemove = new ArrayList<>();
        List<UserPlaces> userPlacesFilteredByUserId = utilitiesService.findUserPlaces(user);
        for (int id : tagIds) {
            for (UserPlaces each : userPlacesFilteredByUserId) {
                List<Tag> userPlacesTags = each.getTags();
                for (Tag tag : userPlacesTags) {
                    if (id == tag.getId()) {
                        tagsToRemove.add(tag);
                    }
                }
                for (Tag t : tagsToRemove){
                    List<Tag> tagList = each.getTags();
                    tagList.remove(t);
                    userPlacesDAO.save(each);
                }
            }
        }

    }

    public void deleteTag(int [] tagList){
        for (int each : tagList){
            tagDAO.deleteById(each);
        }
    }

}
