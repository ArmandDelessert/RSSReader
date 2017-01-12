package hes_so.rssreader;

import java.util.ArrayList;
import java.util.List;

import hes_so.rssreader.saxrssreader.RssFeed;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 28.12.2016
 */

class Feeds {

    private static final Feeds instance = new Feeds();
    public static Feeds getInstance() {
        return instance;
    }

    private static List<RssFeed> rssFeeds;

    private Feeds() {
        rssFeeds = new ArrayList<>();
    }

    static void setRssFeeds(List<RssFeed> rssFeeds) {
        Feeds.rssFeeds = rssFeeds;
    }

    static List<RssFeed> getRssFeeds() {
        return Feeds.rssFeeds;
    }

    static void clearRssFeeds() {
        Feeds.rssFeeds.clear();
    }

}
