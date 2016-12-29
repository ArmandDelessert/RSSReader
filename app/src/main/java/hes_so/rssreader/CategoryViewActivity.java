package hes_so.rssreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hes_so.rssreader.saxrssreader.RssFeed;
import hes_so.rssreader.saxrssreader.RssReader;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 01.12.2016
 */

public class CategoryViewActivity extends AppCompatActivity {

    private RssReaderAsyncTask asyncTask;
    private List<RssFeed> rssFeeds;
    private List<String> listTestFeed = new ArrayList<>(Arrays.asList(
            "http://korben.info/feed",
            "http://feeds.feedburner.com/lerendezvoustech",
            "http://feeds.feedburner.com/lerendezvousjeux",
            "http://www.numerama.com/feed/"
    ));

    // Attributs de la vue
    private ListView feeds_ListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        asyncTask = new RssReaderAsyncTask();
        rssFeeds = new ArrayList<>(1);

        // Création de la vue
        setContentView(R.layout.activity_category_view);

        // Toolbar (without the return button)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        feeds_ListView = (ListView) findViewById(R.id.categoryView_feeds_ListView);
        feeds_ListView.setAdapter(new FeedListAdapter(this, this.rssFeeds));
        feeds_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FeedViewActivity.class);
                intent.putExtra("selectedFeed", position);
                startActivity(intent);
            }
        });



        Button launchParsing_Button = (Button) findViewById(R.id.categoryView_launchParsing_Button);
        launchParsing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouverture des flux RSS
                asyncTask.openRssFeeds(listTestFeed);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Inflate custom toolbar with menu icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
/*
            case android.R.id.refreshFeeds:
                // Ouverture des flux RSS
                asyncTask.openRssFeeds(listTestFeed);
                return true;
*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAsyncTaskFinished(final List<RssFeed> rssFeeds) {
        this.rssFeeds = rssFeeds;
        Feeds.setRssFeeds(rssFeeds); // TODO Feeds

        // Mise à jour de la liste des flux RSS
        synchronized (feeds_ListView.getAdapter()) {
            ((FeedListAdapter) feeds_ListView.getAdapter()).setItems(rssFeeds);
        }

        Toast.makeText(this, "Liste des flux RSS mise à jour.", Toast.LENGTH_SHORT).show();
    }


    /**
     * Ouverture d'un ou de plusieurs flux RSS en tâche de fond.
     */
    class RssReaderAsyncTask extends AsyncTask<URL, Void, List<RssFeed>> {

        /**
         * Ouverture d'un flux RSS.
         *
         * @param url
         */
        public void openRssFeed(String url) {
            try {
                this.execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Ouverture d'une liste de flux RSS.
         *
         * @param urls
         */
        public void openRssFeeds(List<String> urls) {
            List<URL> urlList = new LinkedList<>();

            for (String url : urls) {
                try {
                    urlList.add(new URL(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            this.execute(urlList.toArray(new URL[urlList.size()]));
        }

        /**
         * Téléchargement en tâche de fond.
         *
         * @param urls
         * @return
         */
        @Override
        protected List<RssFeed> doInBackground(URL... urls) {
            RssReader rssReader = new RssReader();
            List<RssFeed> rssFeeds = new LinkedList<>();

            try {
                for (URL url : urls) {
                    rssFeeds.add(rssReader.read(url));
                }
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }

            return rssFeeds;
        }

        /**
         * Fin du téléchargement.
         *
         * @param rssFeeds
         */
        @Override
        protected void onPostExecute(List<RssFeed> rssFeeds) {
            onAsyncTaskFinished(rssFeeds);
        }
    }

}
