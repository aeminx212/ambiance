package com.aMinx.ambiance.scraper;

public class Article {

    private String href;

    private String title;

    private String imageSrc;

    public Article() {
    }

    public Article(String href, String title, String imageSrc) {
        this.href = href;
        this.title = title;
        this.imageSrc = imageSrc;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
