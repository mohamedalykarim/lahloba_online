package online.lahloba.www.lahloba.ui.seller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import online.lahloba.www.lahloba.R;

public class SellerOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_order_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerOrdersFragment.newInstance())
                    .commitNow();
        }
    }
}
