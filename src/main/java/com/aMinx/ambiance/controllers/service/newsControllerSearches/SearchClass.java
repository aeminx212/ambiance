package com.aMinx.ambiance.controllers.service.newsControllerSearches;

import java.util.List;
import java.util.Map;

public interface SearchClass {

    List<String> getSearchWords();
    String getImage();
    String getTitle();
    Map<String, String> getUrlsAndItemsToSelect();
}
