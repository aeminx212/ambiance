package com.aMinx.ambiance.controllerTests;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.controllers.TagController;
import com.aMinx.ambiance.controllers.service.TagService;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.UserPlaces;
import com.aMinx.ambiance.models.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class TagControllerTest {

    @InjectMocks
    TagController tagController;

    @Mock
    UtilitiesService utilitiesService;

    @Mock
    TagService tagService;

    @Mock
    Tag tag;

    @Mock
    UserPlaces userPlaces;

    @Mock
    User user;

    @Mock
    Errors errors;

    @Mock
    TagDTO tagDTO;

    @Mock
    ExtendedModelMap model;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateManageTags(){
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserTagsFromSession()).thenReturn(tags);
        assertThat("tags/manageTags", is(tagController.createManageTags(model)));
    }

    @Test
    public void testProcessManageTags(){
        List<UserPlaces> ups = new ArrayList<>();
        ups.add(userPlaces);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(ups);
        assertThat("tags/manageTags", is(tagController.processManageTags(tag.getName(),model)));
    }

    @Test
    public void testCreateAddTagToPlaces(){
        List<UserPlaces> ups = new ArrayList<>();
        ups.add(userPlaces);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(utilitiesService.findUserPlaces(user)).thenReturn(ups);
        assertThat("tags/addTagsToPlaces", is(tagController.createAddTagsToPlaces(model)));
    }

    @Test
    public void testProcessAddTagToPlaces_NullTagIds(){
        int [] placeIds = {1,2,3,4,5};
        assertThat("tags/addTagsToPlaces", is(tagController.processAddTagToPlaces(null, placeIds, model)));
    }

    @Test
    public void testProcessAddTagToPlaces_NullPlaceIds(){
        int [] tagIds = {1,2,3,4,5};
        assertThat("tags/addTagsToPlaces", is(tagController.processAddTagToPlaces(tagIds, null, model)));
    }

    @Test
    public void testProcessAddTagToPlaces(){
        int [] tagIds = {1,2,3,4,5};
        int [] placeIds = {6,7,8,9,10};
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        assertThat( "redirect:../tags/manageTags", is(tagController.processAddTagToPlaces(tagIds, placeIds, model)));
    }

    @Test
    public void testCreateNewTagPage(){
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserTagsFromSession()).thenReturn(tags);
        assertThat( "tags/createTag", is(tagController.createNewTag(model)));
    }

    @Test
    public void testProcessNewTag_HasErrors() {
        Tag errorTag = new Tag("");
        when(errors.hasErrors()).thenReturn(true);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        assertThat("tags/createTag", is(tagController.processNewTag(errorTag, errors, model)));
    }

    @Test
    public void testProcessNewTag_NameInDatabase(){
        List<Tag> tags = new ArrayList<>();
        Tag dTag = new Tag("Dinner");
        tags.add(dTag);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(user.getTags()).thenReturn(tags);
        when(tag.getName()).thenReturn("Dinner");
        assertThat("tags/createTag", is(tagController.processNewTag(tag, errors, model)));
    }

    @Test
    public void testProcessNewTag(){
        List<Tag> tags = new ArrayList<>();
        Tag dTag = new Tag("Dinner");
        tags.add(dTag);
        when(utilitiesService.getUserFromSession()).thenReturn(user);
        when(user.getTags()).thenReturn(tags);
        assertThat("redirect:/tags/manageTags", is(tagController.processNewTag(tag, errors, model)));
    }

    @Test
    public void testCreateEditTag(){
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserTagsFromSession()).thenReturn(tags);
        assertThat( "tags/editTags", is(tagController.createEditTag(model)));
    }

    @Test
    public void testProcessEditTag_HasErrors(){
        when(errors.hasErrors()).thenReturn(true);
        assertThat( "editTags", is(tagController.processEditTag(tag.getId(),tagDTO, errors, model)));
    }

    @Test
    public void testProcessEditTag_UpdateFalse(){
        int tagToEdit = tag.getId();
        when(tagService.updateTag(tagToEdit, tagDTO)).thenReturn(false);
        assertThat( "redirect:../tags/manageTags", is(tagController.processEditTag(tagToEdit,tagDTO, errors,model)));
    }

    @Test
    public void testProcessEditTag(){
        int tagToEdit = tag.getId();
        when(tagService.updateTag(tagToEdit, tagDTO)).thenReturn(true);
        assertThat( "redirect:../tags/manageTags", is(tagController.processEditTag(tagToEdit,tagDTO, errors,model)));
    }

    @Test
    public void testCreateDeleteTags(){
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(utilitiesService.getUserTagsFromSession()).thenReturn(tags);
        assertThat( "tags/deleteTags", is(tagController.createDeleteTags(model)));
    }

    @Test
    public void testProcessDeleteTag_NullInput(){
        assertThat( "tags/deleteTags", is(tagController.processDeleteTag(null)));
    }

    @Test
    public void testProcessDeleteTagForm(){
        int [] tagIds = {3,4,5};
        assertThat( "redirect:../user/dashboard", is(tagController.processDeleteTag(tagIds)));
    }

}
