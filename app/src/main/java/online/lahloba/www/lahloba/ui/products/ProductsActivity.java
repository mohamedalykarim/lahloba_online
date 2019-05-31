package online.lahloba.www.lahloba.ui.products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_SUBTITLE_ID)){
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_SUBTITLE_ID, intent.getStringExtra(EXTRA_SUBTITLE_ID));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ProductsFragment.newInstance(bundle))
                        .commitNow();
            }

        }


    }
}
