package com.aMinx.ambiance.controllerTests;

import com.aMinx.ambiance.controllers.NewsController;
import com.aMinx.ambiance.controllers.service.NewsService;
import com.aMinx.ambiance.controllers.service.newsControllerSearches.SearchClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NewsControllerTest {

    @InjectMocks
    NewsController newsController;

    @Mock
    NewsService newsService;

    @Mock
    SearchClass searchClass;

    @Mock
    ExtendedModelMap model;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNews(){
        assertThat("user/news", is(newsController.createNews(model)));
    }


    @Test
    public void testProcessNews(){
        String userAction = "rOpenings";
        assertThat("user/news", is(newsController.processNews(userAction, model)));
    }

    @Test
    public void testSetTitle(){
        NewsController newsControllerMock = mock(NewsController.class);
        doAnswer(invocation ->{
            SearchClass aSearchClass = invocation.getArgument(0);
            boolean searchClassIsEmpty = invocation.getArgument(1);
            ExtendedModelMap aModel = invocation.getArgument(2);

            assertEquals(searchClass, aSearchClass);
            assertEquals(false, searchClassIsEmpty);
            assertEquals(model, aModel);
            return null;
        }).when(newsControllerMock).setTitle(any(SearchClass.class), any(boolean.class), any(ExtendedModelMap.class));
        newsControllerMock.setTitle(searchClass, false, model);
    }


}
