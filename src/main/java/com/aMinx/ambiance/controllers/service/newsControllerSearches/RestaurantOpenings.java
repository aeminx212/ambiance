package com.aMinx.ambiance.controllers.service.newsControllerSearches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantOpenings implements SearchClass{

    private String image = "div.article-thumbnail img[src]";

    private String title = "a[title].article-link";

    private List<String> searchWords = new ArrayList<String>();

    private Map<String, String> urlsAndItemsToSelect = new HashMap<>();

    public RestaurantOpenings() {
        searchWords.add("First Look");
        searchWords.add("open");
        List<String> url1 = new ArrayList<>();
        List<String> url2 = new ArrayList<>();
        urlsAndItemsToSelect.put("https://www.saucemagazine.com/tags/new-restaurants","article.article" );
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSearchWords() {
        return searchWords;
    }

    public void setSearchWords(List<String> searchWords) {
        this.searchWords = searchWords;
    }

    public Map<String, String> getUrlsAndItemsToSelect() {
        return urlsAndItemsToSelect;
    }

    public void setUrlsAndItemsToSelect(Map<String, String> urlsAndItemsToSelect) {
        this.urlsAndItemsToSelect = urlsAndItemsToSelect;
    }
}
