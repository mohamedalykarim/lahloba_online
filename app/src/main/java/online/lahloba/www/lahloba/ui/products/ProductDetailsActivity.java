package online.lahloba.www.lahloba.ui.products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.utils.Constants;


public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductDetailsFragment.newInstance())
                    .commitNow();

            Intent intent = getIntent();
            if (intent == null)return;
            if (!intent.hasExtra(Constants.PRODUCT_ID))return;

            ((ProductDetailsFragment) getSupportFragmentManager().getFragments().get(0))
                    .setProductId(intent.getStringExtra(Constants.PRODUCT_ID));
        }
    }
}
