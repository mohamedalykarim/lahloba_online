package online.lahloba.www.lahloba.ui.news;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NewsFragment.newInstance())
                    .commitNow();
        }
    }
}
