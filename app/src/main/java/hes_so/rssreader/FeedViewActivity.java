package hes_so.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import hes_so.rssreader.saxrssreader.RssItem;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class FeedViewActivity extends AppCompatActivity {

    private List<RssItem> rssItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération du flux RSS sélectionné par l'utilisateur
        final int selectedFeed = getIntent().getIntExtra("selectedFeed", -1);
        if (selectedFeed >= 0 && selectedFeed < Feeds.getRssFeeds().size()) {
            this.rssItems = Feeds.getRssFeeds().get(selectedFeed).getRssItems();
        }

        // Creating the view
        setContentView(R.layout.feed_view_activity_layout);

        // Toolbar and return button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Feeds.getRssFeeds().get(selectedFeed).getTitle());
        setSupportActionBar(toolbar);
        // Add the return button to menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView articles_ListView = (ListView) findViewById(R.id.feedView_articles_ListView);
        articles_ListView.setAdapter(new ArticleListAdapter(this, this.rssItems));
        articles_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                intent.putExtra("selectedFeed", selectedFeed);
                intent.putExtra("selectedArticle", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
