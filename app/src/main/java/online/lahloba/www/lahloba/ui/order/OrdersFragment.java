package online.lahloba.www.lahloba.ui.order;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.FragmentOrdersBinding;
import online.lahloba.www.lahloba.ui.adapters.OrdersAdapter;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.comparator.OrderItemDateComparable;

import static online.lahloba.www.lahloba.utils.Constants.ORDER_TYPE_NEW;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    RecyclerView orderRv;
    OrdersAdapter ordersAdapter;
    List<OrderItem> orderItems;
    String currentOrderType = ORDER_TYPE_NEW;


    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentOrdersBinding binding = FragmentOrdersBinding.inflate(inflater,container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this,factory).get(OrdersViewModel.class);

        orderRv = binding.ordersRv;
        ordersAdapter = new OrdersAdapter();
        ordersAdapter.setViewModel(mViewModel);
        orderItems = new ArrayList<>();
        ordersAdapter.setOrderItemList(orderItems);
        orderRv.setAdapter(ordersAdapter);
        orderRv.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.startGetCurrentOrders();
        mViewModel.getCurrentOrders().observe(this, orderItemsList -> {
            if (orderItemsList == null){
                return;
            }


            this.orderItems.clear();
            Collections.sort(orderItemsList, new OrderItemDateComparable());
            this.orderItems.addAll(orderItemsList);
            ordersAdapter.notifyDataSetChanged();

        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setCurrentOrderType(String currentOrderTybe) {
        this.currentOrderType = currentOrderTybe;
    }


}
