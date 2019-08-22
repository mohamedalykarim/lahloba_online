package online.lahloba.www.lahloba.ui.order;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.databinding.OrdersActivityBinding;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrdersActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.orders_activity);
        binding.setLifecycleOwner(this);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Orders");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrdersFragment.newInstance())
                    .commitNow();
        }

    }
}
