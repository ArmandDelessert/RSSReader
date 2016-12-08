package hes_so.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryViewActivity extends AppCompatActivity {

    private TextView goToFeedActivityTextView;
    private ExpandableListView categoryExpandableListView;

    private HandleXML handleXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        goToFeedActivityTextView = (TextView) findViewById(R.id.goToFeedActivityTextView);
//        categoryExpandableListView = (ExpandableListView) findViewById(R.id.categoryExpandableListView);
/*
        List list = new ArrayList<String>("a", "b", "c");
        ExpandableListAdapter categoryExpandableListViewAdapter = new SimpleExpandableListAdapter(this, list, list);
        categoryExpandableListView.setAdapter(categoryExpandableListViewAdapter);
*/
        goToFeedActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedViewActivity.class);
                startActivity(intent);
            }
        });

        Button launchParsing_Buttton = (Button) findViewById(R.id.launchParsing_Buttton);
        launchParsing_Buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTestFeed(); // Ouverture du flux RSS
            }
        });

    }

    private void openTestFeed() {

        // Parsage du XML
        handleXML = new HandleXML("http://feeds.feedburner.com/lerendezvousjeux");

        // Téléchargement du fichier et début du parsing
        handleXML.fetchXML();
        while (handleXML.parsingComplete); // Attente sur le parsing du XML

        String rssInfos;
        rssInfos = handleXML.getTitle() + handleXML.getLink() + handleXML.getDescription();

        Toast.makeText(this, rssInfos, Toast.LENGTH_LONG);
    }
}
