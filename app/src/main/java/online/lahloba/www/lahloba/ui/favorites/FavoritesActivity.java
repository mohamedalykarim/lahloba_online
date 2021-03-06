package online.lahloba.www.lahloba.ui.favorites;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;


public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FavoritesFragment.newInstance())
                    .commitNow();
        }
    }
}
