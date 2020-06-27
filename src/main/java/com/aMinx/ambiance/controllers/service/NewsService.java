package com.aMinx.ambiance.controllers.service;

import com.aMinx.ambiance.scraper.Article;
import com.aMinx.ambiance.scraper.Extractor;
import com.aMinx.ambiance.controllers.service.newsControllerSearches.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
public class NewsService {

    public SearchClass respondToButtonSelection(String userAction){
        SearchClass searchClass = null;
        switch(userAction){
            case "rOpenings":
                searchClass = new RestaurantOpenings();
                break;
            case "beerNews":
                searchClass = new BeerNews();
                break;
            case "wineNews":
                searchClass = new WineNews();
                break;
            case "reviews":
                searchClass = new Reviews();
                break;
        }
        return searchClass;
    }

    public Set<Article> collectArticles(SearchClass searchClass) {
        Set<Article> allArticles = new HashSet<>();
        for (String each : searchClass.getSearchWords()) {
            Set<Article> articles = new HashSet<>();
            Extractor extractor = new Extractor(each, searchClass.getImage(), searchClass.getTitle());
            Iterator<Map.Entry<String, String>> itr = searchClass.getUrlsAndItemsToSelect().entrySet().iterator();

            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                articles = extractor.getPageLinks(entry.getKey(), entry.getValue());
            }
            int count = 0;
            for (Article article : articles) {
                for (Article articleInSet : allArticles) {
                    if (articleInSet.getTitle().equals(article.getTitle())) {
                        count++;
                    }
                }
                if (count <= 0) {
                    allArticles.add(article);
                }
            }
        }
        return allArticles;
    }

    public boolean isArticleSetEmpty(SearchClass searchClass){
        return collectArticles(searchClass).isEmpty();
    }

}
