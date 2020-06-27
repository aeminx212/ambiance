package com.aMinx.ambiance.scraperTests;

import com.aMinx.ambiance.scraper.Article;
import com.aMinx.ambiance.scraper.Extractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExtractorTest {

    @InjectMocks
    Extractor extractor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPageLinks(){
        String url = "https://www.saucemagazine.com/tags/beer";
        String itemToSelect = "brew";
        Set <Article> articles = new HashSet<>();
        assertThat(articles, is(extractor.getPageLinks(url, itemToSelect)));
    }
}
