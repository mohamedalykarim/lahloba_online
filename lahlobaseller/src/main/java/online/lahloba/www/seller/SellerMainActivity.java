package online.lahloba.www.seller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.seller.R;
import online.lahloba.www.seller.ui.sellermain.SellerMainFragment;

public class SellerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerMainFragment.newInstance())
                    .commitNow();
        }
    }
}
