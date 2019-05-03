package online.lahloba.www.lahloba.ui.address;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.AddAddressFragment;

public class AddAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddAddressFragment.newInstance())
                    .commitNow();
        }
    }
}
