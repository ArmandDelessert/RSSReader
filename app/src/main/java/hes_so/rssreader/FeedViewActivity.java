package hes_so.rssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by HP on 01.12.2016.
 */

public class FeedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);

        // Ajout du bouton retour dans la barre de menu
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Toast.makeText(this, "FeedViewActivity.onDestroy()", Toast.LENGTH_SHORT).show(); // DEBUG
    }
}
