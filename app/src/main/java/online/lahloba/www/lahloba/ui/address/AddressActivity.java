package online.lahloba.www.lahloba.ui.address;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.AddressFragment;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddressFragment.newInstance())
                    .commitNow();
        }
    }
}
