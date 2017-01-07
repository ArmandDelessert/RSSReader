package hes_so.rssreader;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import hes_so.rssreader.saxrssreader.RssItem;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class ArticleViewActivity extends AppCompatActivity {

    private RssItem rssItem;

    // Attributs de la vue
    private ImageView picture_ImageView;
    private TextView title_TextView;
    private TextView description_TextView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
        //change title in toolbar
        toolbar.setTitle(this.rssItem.getTitle());
        setSupportActionBar(toolbar);
        // Add return button to menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        


        // Link view to XML
        picture_ImageView = (ImageView) findViewById(R.id.articleView_picture_ImageView);
        title_TextView = (TextView) findViewById(R.id.articleView_title_TextView);
        description_TextView = (TextView) findViewById(R.id.articleView_description_TextView);

        // Affichage de l'article
        title_TextView.setText(this.rssItem.getTitle());
        // remove html from text
        String rawText = this.rssItem.getDescription();
        String Text = Html.fromHtml(rawText).toString().replace((char) 65532, (char) 32);
        description_TextView.setText(Html.fromHtml(rawText));
        description_TextView.setMovementMethod(LinkMovementMethod.getInstance());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ArticleView Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
