package online.lahloba.www.lahloba.ui.products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.ProductsFragment;
import online.lahloba.www.lahloba.utils.Constants;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(Constants.EXTRA_SUBTITLE_ID)){
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_SUBTITLE_ID, intent.getStringExtra(Constants.EXTRA_SUBTITLE_ID));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ProductsFragment.newInstance(bundle))
                        .commitNow();
            }

        }


    }
}
