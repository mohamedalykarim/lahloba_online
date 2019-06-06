package online.lahloba.www.lahloba.ui.order;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.databinding.OrdersActivityBinding;
import online.lahloba.www.lahloba.utils.Constants;

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

            binding.orderTypeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {


                    if (tab.getPosition() == 0){
                        ((OrdersFragment)getSupportFragmentManager().getFragments().get(0))
                            .setCurrentOrderType(Constants.ORDER_TYPE_NEW);

                    }else if (tab.getPosition() == 1){
                        ((OrdersFragment)getSupportFragmentManager().getFragments().get(0))
                            .setCurrentOrderType(Constants.ORDER_TYPE_ONGOING);

                    }else if (tab.getPosition() == 2){
                        ((OrdersFragment)getSupportFragmentManager().getFragments().get(0))
                            .setCurrentOrderType(Constants.ORDER_TYPE_OLD);

                    }

                    ((OrdersFragment)getSupportFragmentManager().getFragments().get(0))
                            .selectOrderType();

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


//
        }




    }
}
