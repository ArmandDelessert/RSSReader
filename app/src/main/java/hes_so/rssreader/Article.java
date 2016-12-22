package hes_so.rssreader;

import java.io.Serializable;

/**
 * Created by edward on 12/8/2016.
 *
 */

public class Article implements Serializable {

    String Title;
    String Content;
    String picturePath;

    Article(String title,String content, String picturePath){
        this.Title = title;
        this.Content=content;
        this.picturePath = picturePath;
    }


    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}


