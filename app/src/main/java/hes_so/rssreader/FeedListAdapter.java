package hes_so.rssreader;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hes_so.rssreader.saxrssreader.RssFeed;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 28.12.2016
 */

public class FeedListAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater;
    private List<RssFeed> rssFeedList;

    public FeedListAdapter(Activity activity, List<RssFeed> rssFeedList) {
        layoutInflater = activity.getLayoutInflater();
        this.rssFeedList = rssFeedList;
    }

    @Override
    public int getCount() {
        return rssFeedList.size();
    }

    @Override
    public Object getItem(int i) {
        return rssFeedList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.feed_list_adapter, null);
        }

        TextView title_TextView = (TextView) view.findViewById(R.id.feedListAdapter_title_TextView);
        TextView description_TextView = (TextView) view.findViewById(R.id.feedListAdapter_description_TextView);

        title_TextView.setText(rssFeedList.get(position).getTitle());
        // Remove HTML from text
        String rawText = rssFeedList.get(position).getDescription();
        String text = Html.fromHtml(rawText).toString();
        description_TextView.setText(text);

        return view;
    }

    public void setItems(List<RssFeed> rssFeedList) {
        this.rssFeedList = rssFeedList;
        notifyDataSetChanged();
    }

}
