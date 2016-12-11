package hes_so.rssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        // toolbar and return button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //add return button to menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // link view to xml
        ArticleView_ImageView = (ImageView) findViewById(R.id.ArticleView_ImageView);
        ArticleViewTitle_TextView = (TextView) findViewById(R.id.ArticleViewTitle_TextView);
        ArticleViewContent_TextView = (TextView) findViewById(R.id.ArticleViewContent_TextView);

    }

    // inflate custom toolbar with menu icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Toast.makeText(this, "ArticleActivity destroyed", Toast.LENGTH_SHORT).show(); // DEBUG
    }

    // toolbar item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // case return button
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
