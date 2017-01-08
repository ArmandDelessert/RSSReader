package hes_so.rssreader;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hes_so.rssreader.saxrssreader.RssItem;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 28.12.2016
 */

public class ArticleListAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater;
    private List<RssItem> rssItemList;

    public ArticleListAdapter(Activity activity, List<RssItem> rssItemList) {
        layoutInflater = activity.getLayoutInflater();
        this.rssItemList = rssItemList;
    }

    @Override
    public int getCount() {
        return rssItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return rssItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.article_list_adapter, null);
        }

        TextView title_TextView = (TextView) view.findViewById(R.id.articleListAdapter_title_TextView);
        TextView description_TextView = (TextView) view.findViewById(R.id.articleListAdapter_description_TextView);

        title_TextView.setText(rssItemList.get(position).getTitle());
        // Remove HTML from text
        String rawText = rssItemList.get(position).getDescription();
        String text = Html.fromHtml(rawText).toString();
        description_TextView.setText(text);

        return view;
    }

    public void setItems(List<RssItem> rssItemList) {
        this.rssItemList = rssItemList;
    }

}
