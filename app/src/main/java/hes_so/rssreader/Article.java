package hes_so.rssreader;

import java.io.Serializable;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class Article implements Serializable {

    private String title;
    private String content;
    private String picturePath;


    public Article(String title, String content, String picturePath) {
        setTitle(title);
        setContent(content);
        setPicturePath(picturePath);
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
