package hes_so.rssreader;

import java.util.LinkedList;
import java.util.List;

import hes_so.rssreader.saxrssreader.RssFeed;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class Feed {

    private String title;
    private String description;
    private String link;

    private List<Article> articles;


    public Feed() {
        articles = new LinkedList<>();
    }

    public Feed(String title, String description, String link, List<Article> articles) {
        this();

        setTitle(title);
        setDescription(description);
        setLink(link);
        setArticles(articles);
    }

    public Feed(RssFeed rssFeed) {
        rssFeed.getTitle();
        rssFeed.getDescription();
        rssFeed.getLink();
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }


    public List<Article> getArticles() {
        return articles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
