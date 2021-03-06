package hes_so.rssreader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    private List<String> rssFeedUrlList;
    private final List<String> rssFeedUrlBaseList = new ArrayList<>(Arrays.asList(
            "korben.info/feed",
            "http://www.numerama.com/feed/",
            "http://www.gamekult.com/feeds/actu.html",
            "http://feeds.feedburner.com/lerendezvoustech",
            "http://feeds.feedburner.com/lerendezvousjeux",
            "http://www.livetile.fr/feed/"
    ));
    private final String rssFeedListFileName = "RssFeedList.RSSReader";
    private Dialog dialog;

    // Attributs de la vue
    private ListView feeds_ListView;
    private ImageView imageViewForRefreshAnimation;
    private Animation refreshFeedsButtonAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openingRssFeeds = false;

        // Création de la vue
        setContentView(R.layout.category_view_activity_layout);

        // Toolbar (without the return button)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Création de la liste des flux RSS
        feeds_ListView = (ListView) findViewById(R.id.categoryView_feeds_ListView);
        feeds_ListView.setAdapter(new FeedListAdapter(this, Feeds.getRssFeeds()));
        feeds_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FeedViewActivity.class);
                intent.putExtra("selectedFeed", position);
                startActivity(intent);
            }
        });

        // Menu contextuel de la feeds_ListView
        registerForContextMenu(feeds_ListView);

        // Création du la boite de dialogue d'ajout d'un flux RSS
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_feed_dialog);
//        dialog.setTitle("Add new RSS feed"); // TODO: Auncun effet
        dialog.findViewById(R.id.newFeedView_Ok_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ajout du flux entré par l'utilisateur
                String newFeedUrl = ((EditText)dialog.findViewById(R.id.newFeedView_FeedUrl_EditText)).getText().toString();
                addFeed(newFeedUrl);

                // Mise à jour de la liste des flux RSS
                new RssReaderAsyncTask().openRssFeeds();

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

        // Lecture ou création du fichier de sauvegarde des flux RSS
        if (doesFileExistAndNotEmpty(rssFeedListFileName)) {
            rssFeedUrlList = readFromFile(rssFeedListFileName);
        }
        else {
            rssFeedUrlList = rssFeedUrlBaseList;
            writeToFile(rssFeedUrlList, rssFeedListFileName);
        }

        // Mise à jour automatique des flux RSS à l'ouverture de l'application
        new RssReaderAsyncTask().openRssFeeds();
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
        imageViewForRefreshAnimation.startAnimation(refreshFeedsButtonAnimation);

        imageViewForRefreshAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mise à jour des flux RSS
                new RssReaderAsyncTask().openRssFeeds();
            }
        });

        return true;
    }

    /**
     * Sélection d'un élément de la barre de menu.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshFeeds_Button:
                // Mise à jour des flux RSS
                new RssReaderAsyncTask().openRssFeeds();
                return true;
            case R.id.addFeed_Button:
                // Ouverture de la boite de dialogue d'ajout d'un flux RSS
                showAddFeedDialog();
                return true;
            case R.id.deleteAllRssFeeds_Button:
                Feeds.clearRssFeeds();
                rssFeedUrlList.clear();
                clearFile(rssFeedListFileName);
                updateFeedsListView();
                // Mise à jour des flux RSS
                new RssReaderAsyncTask().openRssFeeds(); // Permet d'effacer le contenu du fichier de sauvegarde
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
            menu.setHeaderTitle(Feeds.getRssFeeds().get(menuInfoAdapter.position).getTitle());
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
                // TODO
                break;
            case 1: // Delete
                removeFeed(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                updateFeedsListView();
                break;
            default:
        }

        return true;
    }

    private void showAddFeedDialog() {
        dialog.show();
    }

    private void addFeed(String feedUrl) {
        rssFeedUrlList.add(feedUrl);
    }

    private void removeFeed(int position) {
        rssFeedUrlList.remove(position);
        Feeds.getRssFeeds().remove(position);

        // Sauvegarde de la liste des flux RSS
        writeToFile(rssFeedUrlList, rssFeedListFileName);
    }

    private void onAsyncTaskFinished(final List<RssFeed> rssFeeds) {
        Feeds.setRssFeeds(rssFeeds);

        // Mise à jour de la liste des flux RSS
        updateFeedsListView();

        // Sauvegarde de la liste des flux RSS
        writeToFile(rssFeedUrlList, rssFeedListFileName);

        Toast.makeText(this, R.string.rssFeedsUpdate_Toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mise à jour de la liste des flux RSS.
     */
    private void updateFeedsListView() {
        synchronized (feeds_ListView.getAdapter()) {
            ((FeedListAdapter) feeds_ListView.getAdapter()).setItems(Feeds.getRssFeeds());
        }
    }

    /**
     * Enregistrement des flux RSS dans un fichier de sauvegarde.
     *
     * @param rssFeedList
     */
    private void writeToFile(List<String> rssFeedList, String fileName) {
        try {
            Writer outputWriter = new BufferedWriter(new OutputStreamWriter(getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE)));

            for (String rssFeed : rssFeedList) {
                outputWriter.write(rssFeed + System.lineSeparator());
            }

            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean clearFile(String fileName) {
        return new File(fileName).delete();
    }

    /**
     * Lecture des flux RSS depuis le fichier de sauvegarde.
     *
     * @return
     */
    private List<String> readFromFile(String fileName) {
        List<String> rssFeedList = new LinkedList<>();

        try {
            InputStream inputStream = getApplicationContext().openFileInput(fileName);

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String string;

                while ((string = bufferedReader.readLine()) != null) {
                    rssFeedList.add(string);
                }

                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rssFeedList;
    }

    private boolean doesFileExistAndNotEmpty(String fileName) {
        return getApplicationContext().getFileStreamPath(fileName).length() > 0;
    }


    /**
     * Ouverture d'un ou de plusieurs flux RSS en tâche de fond.
     */
    class RssReaderAsyncTask extends AsyncTask<URL, Void, List<RssFeed>> {

        final Activity parent = CategoryViewActivity.this;


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
         */
        public void openRssFeeds() {
            List<URL> urlList = new LinkedList<>();

            // Lecture des URL
            if (rssFeedUrlList != null) {
                for (String url : rssFeedUrlList) {
                    try {
                        urlList.add(new URL(url));
                    } catch (MalformedURLException e1) {
                        // Ajout automatique du protocole HTTP s'il est manquant
                        if (e1.getMessage().contains("Protocol not found")) {
                            try {
                                urlList.add(new URL("http://" + url));
                            } catch (MalformedURLException e2) {
                                e1.printStackTrace();
                                rssFeedUrlList.remove(url);
                            }
                        } else {
                            e1.printStackTrace();
                        }
                    }
                }
            }

            if (!openingRssFeeds) {
                openingRssFeeds = true;
                this.execute(urlList.toArray(new URL[urlList.size()]));
            }
            else {
                Toast.makeText(CategoryViewActivity.this, R.string.rssFeedsAlreadyUpdating_Toast, Toast.LENGTH_SHORT).show();
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
            List<RssFeed> rssFeeds = new LinkedList<>();

            // Récupération des fichiers XML aux URL spécifées
            List<String> listError = new LinkedList<>();
            for (URL url : urls) {
                try {
                    rssFeeds.add(RssReader.read(url));
                } catch (SAXException | IOException e) {
                    e.printStackTrace();

                    listError.add(url.toString());
                }
            }

            if (listError.size() > 0) {
                final String finalErrors = listError.toString().replace("[", "").replace("]", "").replace(", ", "\n");
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CategoryViewActivity.this,
                                getString(R.string.rssFeedsParsingError_Toast) +
                                        finalErrors,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return rssFeeds;
        }

        @Override
        protected void onPreExecute() {
            // Démarrage de l'animation du bouton refresh
            if (imageViewForRefreshAnimation != null) {
                imageViewForRefreshAnimation.startAnimation(refreshFeedsButtonAnimation);
            }
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
            if (imageViewForRefreshAnimation != null) {
                imageViewForRefreshAnimation.clearAnimation();
            }
        }
    }

}
