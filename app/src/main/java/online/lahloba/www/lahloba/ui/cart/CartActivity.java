package online.lahloba.www.lahloba.ui.cart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.CartFragment;
import online.lahloba.www.lahloba.utils.Constants;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CartFragment.newInstance(bundle))
                        .commitNow();
            }

        }
    }
}
