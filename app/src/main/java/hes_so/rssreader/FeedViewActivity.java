package hes_so.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by HP on 01.12.2016.
 */

public class FeedViewActivity extends AppCompatActivity {
    private TextView goToArticleActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);

        goToArticleActivityTextView = (TextView) findViewById(R.id.goToArticleActivityTextView);

        // Ajout du bouton retour dans la barre de menu
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        goToArticleActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Toast.makeText(this, "FeedViewActivity.onDestroy()", Toast.LENGTH_SHORT).show(); // DEBUG
    }
}
