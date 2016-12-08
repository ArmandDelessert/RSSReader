package hes_so.rssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HP on 01.12.2016.
 */

public class ArticleViewActivity extends AppCompatActivity {
    //view (same name as xml)
    ImageView ArticleView_ImageView;
    TextView ArticleViewTitle_TextView;
    TextView ArticleViewContent_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        // Ajout de la barre de menu
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // link view to xml
        ArticleView_ImageView = (ImageView) findViewById(R.id.ArticleView_ImageView);
        ArticleViewTitle_TextView = (TextView) findViewById(R.id.ArticleViewTitle_TextView);
        ArticleViewContent_TextView = (TextView) findViewById(R.id.ArticleViewContent_TextView);

    }
}
