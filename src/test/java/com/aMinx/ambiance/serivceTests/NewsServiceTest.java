package com.aMinx.ambiance.serivceTests;

import com.aMinx.ambiance.scraper.Article;

import com.aMinx.ambiance.controllers.service.NewsService;
import com.aMinx.ambiance.controllers.service.newsControllerSearches.Reviews;
import com.aMinx.ambiance.controllers.service.newsControllerSearches.SearchClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;

import java.util.HashSet;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NewsServiceTest {

    @InjectMocks
    NewsService newsService;

    @Mock
    SearchClass searchClass;

    @Mock
    SearchClass reviews;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRespondToButton_SelectionNull(){
        SearchClass searchClass = null;
        assertThat(searchClass, is(newsService.respondToButtonSelection("")));
    }

    @Test
    public void testRespondToButtonSelection(){
        String reviewsStr = "reviews";
        NewsService newsServiceMock = mock(NewsService.class);
        doAnswer(invocation ->{
            String reviewsStrMock = invocation.getArgument(0);

            assertEquals(reviewsStr, reviewsStrMock);
            return reviews;
        }).when(newsServiceMock).respondToButtonSelection(reviewsStr);
        newsService.respondToButtonSelection(reviewsStr);
    }

    @Test
    public void testCollectArticles(){
        assertThat(new HashSet<Article>(), is(newsService.collectArticles(searchClass)));
    }

    @Test
    public void testIsArticleSetEmpty_True(){
        assertThat(true, is(newsService.isArticleSetEmpty(searchClass)));
    }

    @Test
    public void testIsArticleSetEmpty_False(){
        SearchClass searchClass = new Reviews();
        assertThat(false, is(newsService.isArticleSetEmpty(searchClass)));
    }

}
