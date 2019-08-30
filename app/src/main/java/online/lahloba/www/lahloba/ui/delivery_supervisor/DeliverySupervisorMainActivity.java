package online.lahloba.www.lahloba.ui.delivery_supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.ui.adapters.DeliverSupervisorAdapter;
import online.lahloba.www.lahloba.ui.adapters.DeliverySDeliveryAdapter;


public class DeliverySupervisorMainActivity extends AppCompatActivity implements
        DeliverSupervisorAdapter.DeliverySupervisorAdapterClick,
        DeliverySDeliveryAdapter.DeliverySDeliveryAdapterClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_supervisor_main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DeliverySupervisorMainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onDeliverySupervisorAdapterClick(OrderItem orderItem) {
        ((DeliverySupervisorMainFragment)getSupportFragmentManager().getFragments().get(0))
                .onDeliverySupervisorAdapterClick(orderItem);
    }

    @Override
    public void onDeliverySDeliveryAdapterClick() {
        ((DeliverySupervisorMainFragment)getSupportFragmentManager().getFragments().get(0))
                .onDeliverySDeliveryAdapterClick();

    }
}
