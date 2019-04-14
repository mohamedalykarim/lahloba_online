package online.lahloba.www.lahloba.ui.products;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.ProductsFragment;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductsFragment.newInstance())
                    .commitNow();
        }
    }
}
