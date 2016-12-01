package hes_so.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CategoryViewActivity extends AppCompatActivity {

    private TextView goToFeedActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        goToFeedActivityTextView = (TextView) findViewById(R.id.goToFeedActivityTextView);

        goToFeedActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
