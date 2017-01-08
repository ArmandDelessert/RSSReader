package hes_so.rssreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import hes_so.rssreader.saxrssreader.RssItem;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class ArticleViewActivity extends AppCompatActivity {

    private RssItem rssItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération de l'article sélectionné par l'utilisateur
        int selectedFeed = getIntent().getIntExtra("selectedFeed", -1);
        int selectedArticle = getIntent().getIntExtra("selectedArticle", -1);
        this.rssItem = Feeds.getRssFeeds().get(selectedFeed).getRssItems().get(selectedArticle);

        // Creating the view
        setContentView(R.layout.article_view_activity_layout);

        // Toolbar and return button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Feeds.getRssFeeds().get(selectedFeed).getTitle());
        setSupportActionBar(toolbar);
        // Add return button to menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Link view to XML
        TextView title_TextView = (TextView) findViewById(R.id.articleView_title_TextView);
        TextView description_TextView = (TextView) findViewById(R.id.articleView_description_TextView);

        // Affichage de l'article
        title_TextView.setText(this.rssItem.getTitle());
        // Remove HTML from text
        String rawText = this.rssItem.getDescription();
        description_TextView.setText(Html.fromHtml(rawText));
        description_TextView.setMovementMethod(LinkMovementMethod.getInstance());
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
