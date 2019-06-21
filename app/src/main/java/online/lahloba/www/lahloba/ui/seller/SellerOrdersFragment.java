package online.lahloba.www.lahloba.ui.seller;

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

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.SellerOrderFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.OrdersAdapter;
import online.lahloba.www.lahloba.utils.Injector;


public class SellerOrdersFragment extends Fragment {

    private SellerOrdersViewModel mViewModel;

    public static SellerOrdersFragment newInstance() {
        return new SellerOrdersFragment();
    }
    RecyclerView orderRv;
    OrdersAdapter adapter;
    List<OrderItem> orderItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SellerOrderFragmentBinding binding = SellerOrderFragmentBinding.inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerOrdersViewModel.class);


        orderItems = new ArrayList<>();

        adapter = new OrdersAdapter();
        adapter.setOrderItemList(orderItems);

        orderRv = binding.orderRv;
        orderRv.setAdapter(adapter);
        orderRv.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRv.setHasFixedSize(true);

        mViewModel.startGetSellerOrders();


        return binding.getRoot();
    }


}
