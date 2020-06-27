package com.aMinx.ambiance.scraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Extractor {

    private Set<Article> articles;
    private String searchWord;
    private String image;
    private String titleToFind;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    public Extractor(String searchWord, String image, String titleToFind) {
        this.searchWord = searchWord;
        this.image = image;
        this.titleToFind = titleToFind;
        articles = new HashSet<>();
    }

// selects links from page and return article info from search
    public Set<Article> getPageLinks(String url, String itemToSelect) {

            try {
                Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
                Document document = connection.get();
                Elements otherLinks = document.select(itemToSelect);

                for (Element link : otherLinks) {
                    String href = link.select("a[href].article-link").attr("abs:href");
                    Elements theTitle = link.select(titleToFind);
                    String titleValue = theTitle.attr("title");

                    Element theImage = link.select(image).first();
                    String srcValue = theImage.attr("src");
                    Article anArticle = new Article(href, titleValue, srcValue);
                    boolean shouldAdd = false;
                    if (!articles.isEmpty()) {
                            if(!articles.contains(anArticle)){
                                if (titleValue.contains(searchWord)){
                                    shouldAdd = true;
                                }
                        }
                        if (shouldAdd) {
                            articles.add(anArticle);
                        }
                    } else {
                        if (titleValue.contains(searchWord)) {
                            articles.add(anArticle);
                        }
                    }

                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        return articles;
    }
}
