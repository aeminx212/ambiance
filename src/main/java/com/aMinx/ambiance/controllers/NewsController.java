package com.aMinx.ambiance.controllers;

import com.aMinx.ambiance.controllers.service.NewsService;
import com.aMinx.ambiance.controllers.service.newsControllerSearches.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class NewsController {

    @Autowired
    NewsService newsService;

    private String errorMessage = "There has been an error. Please check back later for news.";

    @GetMapping("user/news")
    public String createNews(Model model) {
        SearchClass searchClass = new RestaurantOpenings();
        boolean isSearchClassEmpty = newsService.isArticleSetEmpty(searchClass);
        setTitle(searchClass, isSearchClassEmpty, model);
        return "user/news";
    }

    @PostMapping("user/news")
    public String processNews(@RequestParam String userAction, Model model) {
        SearchClass chosenSearchClass = newsService.respondToButtonSelection(userAction);
        boolean isSearchClassEmpty = newsService.isArticleSetEmpty(chosenSearchClass);
        setTitle(chosenSearchClass, isSearchClassEmpty, model);
        return "user/news";
    }

    public void setTitle(SearchClass searchClass, boolean SearchClassIsEmpty, Model model) {
        if (!SearchClassIsEmpty) {
            model.addAttribute(
                    "allArticles", newsService.collectArticles(searchClass));
            model.addAttribute("title", "All the Latest News");
        } else {
            model.addAttribute("title", errorMessage);
        }
    }
}