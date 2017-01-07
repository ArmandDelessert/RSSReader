package hes_so.rssreader;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

    private boolean openingRssFeeds;
    private List<RssFeed> rssFeeds;
    private List<String> listTestFeed = new ArrayList<>(Arrays.asList(
            "korben.info/feed",
            "http://feeds.feedburner.com/lerendezvoustech",
            "http://feeds.feedburner.com/lerendezvousjeux",
            "http://www.numerama.com/feed/"
    ));
    private AlertDialog.Builder addFeedDialogBuilder;
    private Dialog dialog;

    // Attributs de la vue
    private ListView feeds_ListView;
    private ImageView imageViewForRefreshAnimation;
    private Animation refreshFeedsButtonAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openingRssFeeds = false;
        rssFeeds = new ArrayList<>(1);

        // Création de la vue
        setContentView(R.layout.activity_category_view);

        // Toolbar (without the return button)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Création de la liste des flux RSS
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
        feeds_ListView.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View view) {
                return false;
            }
        });

        // Création du la boite de dialogue d'ajout d'un flux RSS
/*
        addFeedDialogBuilder = new AlertDialog.Builder(this);
        addFeedDialogBuilder.setView(getLayoutInflater().inflate(R.layout.new_feed_view, null));
        addFeedDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addNewFeed(((EditText) findViewById(R.id.newFeedView_FeedUrl_EditText)).getText().toString());
            }
        });
        addFeedDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
*/
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_feed_view);
//        dialog.setTitle("Add new RSS feed"); // TODO: Auncun effet
        dialog.findViewById(R.id.newFeedView_Ok_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFeedUrl = ((EditText)dialog.findViewById(R.id.newFeedView_FeedUrl_EditText)).getText().toString();
                addNewFeed(newFeedUrl);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.newFeedView_Cancel_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);

        // Création du bouton de téléchargement et parsing des flux RSS
        Button launchParsing_Button = (Button) findViewById(R.id.categoryView_launchParsing_Button);
        launchParsing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouverture des flux RSS
                new RssReaderAsyncTask().openRssFeeds(listTestFeed);
            }
        });

        // Mise à jour automatique des flux RSS à l'ouverture de l'application
//        asyncTask.openRssFeeds(listTestFeed);

        Button enableAnimationButton = (Button) findViewById(R.id.enableAnimationButton);
        enableAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewForRefreshAnimation.startAnimation(refreshFeedsButtonAnimation);
            }
        });
        Button disableAnimationButton = (Button) findViewById(R.id.disableAnimationButton);
        disableAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewForRefreshAnimation.clearAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Création de la barre de menu.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Animation du bouton refresh
        imageViewForRefreshAnimation = (ImageView) getLayoutInflater().inflate(R.layout.refresh_button_animation, null);
        refreshFeedsButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.refresh_button_animation);
        menu.findItem(R.id.refreshFeeds_Button).setActionView(imageViewForRefreshAnimation);
//        imageViewForRefreshAnimation.setAnimation(refreshFeedsButtonAnimation);

        return true;
    }

    /**
     * Ouverture du menu contextuel des flux RSS.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshFeeds_Button:
                // Mise à jour des flux RSS
                new RssReaderAsyncTask().openRssFeeds(listTestFeed);
                return true;
            case R.id.addFeed_Button:
                // Ouverture de la boite de dialogue d'ajout d'un flux RSS
                showAddFeedDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Création du menu contextuel des flux RSS.
     *
     * @param menu
     * @param view
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.categoryView_feeds_ListView) {
            AdapterView.AdapterContextMenuInfo menuInfoAdapter = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            menu.setHeaderTitle(Countries[menuInfoAdapter.position]);
            String[] menuItems = getResources().getStringArray(R.array.contextualMenu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    /**
     * Ouverture du menu contextuel des flux RSS.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();

        switch (menuItemIndex) {
            case 0: // Edit

                break;
            case 1: // Delete
                rssFeeds.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                updateFeedsListView();
                break;
            default:
        }

        return true;
    }

    private void showAddFeedDialog() {
//        addFeedDialogBuilder.create().show();
        dialog.show();
    }

    private void addNewFeed(String feedUrl) {
        listTestFeed.add(feedUrl);
    }

    protected void onAsyncTaskFinished(final List<RssFeed> rssFeeds) {
        this.rssFeeds = rssFeeds;
        Feeds.setRssFeeds(rssFeeds); // TODO Feeds

        // Mise à jour de la liste des flux RSS
        updateFeedsListView();

        Toast.makeText(this, "Liste des flux RSS mise à jour.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mise à jour de la liste des flux RSS.
     */
    private void updateFeedsListView() {
        synchronized (feeds_ListView.getAdapter()) {
            ((FeedListAdapter) feeds_ListView.getAdapter()).setItems(rssFeeds);
        }
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
                } catch (MalformedURLException e1) {
                    // Ajout automatique du protocole HTTP s'il est manquant
                    if (e1.getMessage().contains("Protocol not found")) {
                        try {
                            urlList.add(new URL("http://" + url));
                        } catch (MalformedURLException e2) {
                            e1.printStackTrace();
                        }
                    }
                    else {
                        e1.printStackTrace();
                    }
                }
            }

            if (openingRssFeeds == false) {
                openingRssFeeds = true;
                this.execute(urlList.toArray(new URL[urlList.size()]));
            }
            else {
                Toast.makeText(CategoryViewActivity.this, "La mise à jour des flux RSS est déjà en cours…", Toast.LENGTH_SHORT).show();
            }
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

        @Override
        protected void onPreExecute() {
            // Démarrage de l'animation du bouton refresh
            imageViewForRefreshAnimation.startAnimation(refreshFeedsButtonAnimation);
        }

        /**
         * Fin du téléchargement.
         *
         * @param rssFeeds
         */
        @Override
        protected void onPostExecute(List<RssFeed> rssFeeds) {
            openingRssFeeds = false;
            onAsyncTaskFinished(rssFeeds);

            // Arrêt de l'animation du bouton refresh
            imageViewForRefreshAnimation.clearAnimation();
        }
    }

}
