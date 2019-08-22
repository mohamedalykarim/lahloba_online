package online.lahloba.www.lahloba.ui.seller;

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
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MarketPlace;
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
    MarketPlace marketPlace;

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

        mViewModel.startGetSellerOrders(marketPlace.getId());

        mViewModel.getSellerOrder().observe(this, orderItemsList ->{
            if (orderItemsList== null)return;

            orderItems.clear();

            for (OrderItem orderItem: orderItemsList){
                if (orderItem.getMarketplaceId().equals(marketPlace.getId())){
                    orderItems.add(orderItem);
                }
            }

            adapter.notifyDataSetChanged();
        } );



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }
}
