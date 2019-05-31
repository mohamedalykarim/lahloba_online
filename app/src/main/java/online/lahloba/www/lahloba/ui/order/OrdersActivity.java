package online.lahloba.www.lahloba.ui.order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.databinding.OrdersActivityBinding;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrdersActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.orders_activity);
        binding.setLifecycleOwner(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrdersFragment.newInstance())
                    .commitNow();
        }
    }
}
