package hes_so.rssreader;

import java.util.ArrayList;
import java.util.List;

import hes_so.rssreader.saxrssreader.RssFeed;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 28.12.2016
 */

public class Feeds {

    private static Feeds instance = new Feeds();
    public static Feeds getInstance() {
        return instance;
    }

    private static List<RssFeed> rssFeeds;

    private Feeds() {
        this.rssFeeds = new ArrayList<>();
    }

    public static void setRssFeeds(List<RssFeed> rssFeeds) {
        Feeds.rssFeeds = rssFeeds;
    }

    public static List<RssFeed> getRssFeeds() {
        return Feeds.rssFeeds;
    }

}
