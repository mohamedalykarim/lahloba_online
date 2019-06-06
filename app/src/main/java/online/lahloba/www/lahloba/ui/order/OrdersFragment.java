package online.lahloba.www.lahloba.ui.order;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.FragmentOrdersBinding;
import online.lahloba.www.lahloba.ui.adapters.OrdersAdapter;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.OrderStatusUtils;

import static online.lahloba.www.lahloba.utils.Constants.ORDER_TYPE_NEW;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_TYPE_OLD;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_TYPE_ONGOING;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    RecyclerView orderRv;
    OrdersAdapter ordersAdapter;
    List<OrderItem> filteredOrderItems;
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

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this,factory).get(OrdersViewModel.class);

        orderRv = binding.ordersRv;
        ordersAdapter = new OrdersAdapter();
        ordersAdapter.setViewModel(mViewModel);
        filteredOrderItems = new ArrayList<>();
        orderItems = new ArrayList<>();
        ordersAdapter.setOrderItemList(filteredOrderItems);
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
            this.orderItems.addAll(orderItemsList);

            selectOrderType();

        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setCurrentOrderType(String currentOrderTybe) {
        this.currentOrderType = currentOrderTybe;
    }

    public void selectOrderType(){
        filteredOrderItems.clear();
        ordersAdapter.notifyDataSetChanged();

        switch (currentOrderType){
            case ORDER_TYPE_NEW:
                for (OrderItem item : orderItems){
                    if (item.getOrderStatus() == OrderStatusUtils.ORDER_STATUS_PENDING){
                        filteredOrderItems.add(item);
                    }
                    ordersAdapter.notifyDataSetChanged();
                }
                break;

            case ORDER_TYPE_ONGOING:
                for (OrderItem item : orderItems){
                    if (item.getOrderStatus() == OrderStatusUtils.ORDER_STATUS_PROCESSING
                            || item.getOrderStatus() == OrderStatusUtils.ORDER_STATUS_PROCESSED
                            || item.getOrderStatus() == OrderStatusUtils.ORDER_STATUS_SHIPPED){
                        filteredOrderItems.add(item);
                    }
                    ordersAdapter.notifyDataSetChanged();
                }

                break;

            case ORDER_TYPE_OLD:
                for (OrderItem item : orderItems){
                    if (item.getOrderStatus() == OrderStatusUtils.ORDER_STATUS_COMPLETED){
                        filteredOrderItems.add(item);
                    }
                    ordersAdapter.notifyDataSetChanged();
                }


                break;
        }
    }
}
