package online.lahloba.www.lahloba.ui.delivery;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliveryMainFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.DeliveryOrdersAdapter;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.StatusUtils;


public class DeliveryMainFragment extends Fragment {
    List<OrderItem> orderItems;
    private DeliveryMainViewModel mViewModel;
    DeliveryOrdersAdapter adapter;

    public static DeliveryMainFragment newInstance() {
        return new DeliveryMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        DeliveryMainFragmentBinding binding = DeliveryMainFragmentBinding
                .inflate(inflater, container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(DeliveryMainViewModel.class);

        /**
         * Recyclerview
         * Adpater
         */

        orderItems = new ArrayList<>();

        adapter = new DeliveryOrdersAdapter();
        adapter.setOrderItems(orderItems);
        adapter.setmViewModel(mViewModel);

        binding.ordersRV.setAdapter(adapter);
        binding.ordersRV.setLayoutManager(new LinearLayoutManager(getContext()));



        mViewModel.startGetOrdersForDelivery();





        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getOrders().observe(this, items -> {
            if (items == null)return;
            if (items.size() == 0)return;
            if (items.get(0).getDeliveryAllocatedTo() == null)return;


            orderItems.clear();

            if (!items.get(0).getDeliveryAllocatedTo().equals(FirebaseAuth.getInstance().getUid()))
                return;

            for (OrderItem child : items){
                if (child.getOrderStatus() == StatusUtils.ORDER_STATUS_ALLOCATE_TO_DELIVERY ||
                        child.getOrderStatus() == StatusUtils.ORDER_STATUS_ON_THE_WAY){
                    orderItems.add(child);
                }
            }

            adapter.notifyDataSetChanged();

        });
    }
}
