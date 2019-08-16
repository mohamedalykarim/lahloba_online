package online.lahloba.www.lahloba.ui.seller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;


public class SellerProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_products_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerProductsFragment.newInstance())
                    .commitNow();

            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_SUBTITLE_ID)){
                ((SellerProductsFragment)getSupportFragmentManager().getFragments().get(0))
                        .setCategory(intent.getStringExtra(EXTRA_SUBTITLE_ID));
            }
        }
    }
}
