package online.lahloba.www.lahloba.ui.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import online.lahloba.www.lahloba.R;


public class SellerEditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_edit_product_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerEditProductFragment.newInstance())
                    .commitNow();
        }
    }
}
