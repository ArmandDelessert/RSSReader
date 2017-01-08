package hes_so.rssreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import hes_so.rssreader.saxrssreader.RssItem;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class ArticleViewActivity extends AppCompatActivity {

    private RssItem rssItem;

    // Attributs de la vue
//  private ImageView picture_ImageView;
    private TextView title_TextView;
    private TextView description_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération de l'article sélectionné par l'utilisateur
        int selectedFeed = getIntent().getIntExtra("selectedFeed", -1);
        int selectedArticle = getIntent().getIntExtra("selectedArticle", -1);
        this.rssItem = Feeds.getRssFeeds().get(selectedFeed).getRssItems().get(selectedArticle);

        // Creating the view
        setContentView(R.layout.activity_article_view);

        // Toolbar and return button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Feeds.getRssFeeds().get(selectedFeed).getTitle());
        setSupportActionBar(toolbar);
        // Add return button to menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Link view to XML
//      picture_ImageView = (ImageView) findViewById(R.id.articleView_picture_ImageView);
        title_TextView = (TextView) findViewById(R.id.articleView_title_TextView);
        description_TextView = (TextView) findViewById(R.id.articleView_description_TextView);

        // Affichage de l'article
        title_TextView.setText(this.rssItem.getTitle());
        // Remove HTML from text
        String rawText = this.rssItem.getDescription();
        description_TextView.setText(Html.fromHtml(rawText));
        description_TextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
