package com.aMinx.ambiance.serivceTests;

import com.aMinx.ambiance.controllers.service.TagService;
import com.aMinx.ambiance.data.TagDAO;
import com.aMinx.ambiance.models.Tag;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;


public class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    Tag tag;

    @Mock
    TagDTO tagDTO;

    @Mock
    TagDAO tagDAO;

    @Mock
    List<Tag> tags;

    @Mock
    User user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveTagsToUserPlaces(){
        User user = new User();
        int[] tagIds = {1,2,3,4,5};
        int[] placeIds = {1,2,3,4,5};
        TagService tagServiceMock = mock(TagService.class);
        doAnswer(invocation ->{
            User userMock = invocation.getArgument(0);
            int[] tagIdList = invocation.getArgument(1);
            int [] placeList = invocation.getArgument(2);
            List <Tag> tagList = invocation.getArgument(3);

            assertEquals(user, userMock);
            assertEquals(tagIds, tagIdList);
            assertEquals(placeIds, placeList);
            assertEquals(tags, tagList);
            return null;
        }).when(tagServiceMock).savedTagsToUserPlaces(user, tagIds, placeIds, tags);
        tagServiceMock.savedTagsToUserPlaces(user, tagIds, placeIds, tags);
    }

    @Test
    public void testSaveTagWithUser(){
        TagService tagServiceMock = mock(TagService.class);
        doAnswer(invocation ->{
            Tag tagMock = invocation.getArgument(0);
            User userMock = invocation.getArgument(1);

            assertEquals(tag, tagMock);
            assertEquals(user, userMock);
            return null;
        }).when(tagServiceMock).saveTagWithUser(tag, user);
        tagServiceMock.saveTagWithUser(tag, user);
    }

    @Test
    public void testUpdateTag_False(){
        int tagToEdit = tag.getId();
        Optional<Tag> result = Optional.of(tag).empty();
        when(tagDAO.findById(tagToEdit)).thenReturn(result);
        assertThat(false, is(tagService.updateTag(tagToEdit,tagDTO)));
    }

    @Test
    public void testUpdateTag(){
        int tagToEdit = 1;
        when(tagDAO.findById(tagToEdit)).thenReturn(Optional.of(tag));
        assertThat(true, is(tagService.updateTag(1,tagDTO)));
    }

    @Test
    public void testRemoveTagsFromUserPlaces(){
        User user = new User();
        int[] tagIds = {1,2,3,4,5};
        TagService tagServiceMock = mock(TagService.class);
        doAnswer(invocation ->{
            User userMock = invocation.getArgument(0);
            int[] tagList = invocation.getArgument(1);

            assertEquals(user, userMock);
            assertEquals(tagIds, tagList);
            return null;
        }).when(tagServiceMock).removeTagsFromUserPlaces(user, tagIds);
        tagServiceMock.removeTagsFromUserPlaces(user, tagIds);
    }

    @Test
    public void testDeleteTag(){
        int[] tagIds = {1,2,3,4,5};
        TagService tagServiceMock = mock(TagService.class);
        doAnswer(invocation ->{
            int[] tagList = invocation.getArgument(0);

            assertEquals(tagIds, tagList);
            return null;
        }).when(tagServiceMock).deleteTag(tagIds);
        tagServiceMock.deleteTag(tagIds);
    }

}
