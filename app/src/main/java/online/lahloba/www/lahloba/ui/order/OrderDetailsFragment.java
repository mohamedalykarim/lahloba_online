package online.lahloba.www.lahloba.ui.order;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.FragmentOrderDetailsBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.OrderStatusUtils;

import static online.lahloba.www.lahloba.utils.Constants.ORDER_ITEM;

public class OrderDetailsFragment extends Fragment {

    private OrderDetailsViewModel mViewModel;
    private FragmentOrderDetailsBinding binding;
    private LoginViewModel loginViewModel;
    private OrderItem oldOrderItem;

    public static OrderDetailsFragment newInstance() {
        return new OrderDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentOrderDetailsBinding.inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this,factory).get(OrderDetailsViewModel.class);
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);

        binding.setViewModel(mViewModel);



        Intent oldIntent = getActivity().getIntent();
        if (oldIntent == null || !oldIntent.hasExtra(ORDER_ITEM)){
            getActivity().finish();
        }

        oldOrderItem = oldIntent.getParcelableExtra(ORDER_ITEM);

        mViewModel.startGetOrderById(oldOrderItem.getId());
        mViewModel.startGetUserForOrder(oldOrderItem.getUserId());
        mViewModel.startGetMarketPlaceForOrder(oldOrderItem.getMarketplaceId());



        /*
         * Add Products to view
         */
        HashMap<String, CartItem> products = oldOrderItem.getProducts();

        binding.productsContainer.removeAllViews();
        for(CartItem product: products.values()){

            if (product!=null){
                View view = inflater.inflate(R.layout.row_order_list_product,null,false);
                TextView nameTv = view.findViewById(R.id.productNameTv);
                TextView quantityTv = view.findViewById(R.id.productQuantityTv);
                TextView priceTv = view.findViewById(R.id.producPriceTv);

                nameTv.setText(product.getProductName());
                quantityTv.setText("Qty: "+product.getCount());
                priceTv.setText(product.getPrice()+" " + "EGP");


                binding.productsContainer.addView(view);
            }
        }


        binding.reorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startReorder(oldOrderItem);
            }
        });


        binding.preparedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startChangeOrderStatus(oldOrderItem.getId(), OrderStatusUtils.ORDER_STATUS_PREPARED);

            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startChangeOrderStatus(oldOrderItem.getId(), OrderStatusUtils.ORDER_STATUS_CANCELED);
            }
        });



        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getOrderItem().observe(this, orderItem -> {
            if (orderItem == null)return;
            mViewModel.helper.setOrderItem(orderItem);
        });

        loginViewModel.getCurrentUserDetails().observe(this, userItem -> {
            if (userItem == null) return;
            loginViewModel.loginVMHelper.setCurrentUser(userItem);

        });

        mViewModel.getUserForOrder().observe(this, userItem -> {
            if (userItem == null) return;
            binding.setUserForOrder(userItem);
        });


        mViewModel.getMarketplace().observe(this, marketPlace -> {
            if (marketPlace == null) return;

            Log.v("sss first : ", ""+mViewModel.helper.isThisSeller());

            binding.setMarketPlace(marketPlace);

            if ( marketPlace.getId().equals(oldOrderItem.getMarketplaceId())
                    && marketPlace.getSellerId().equals(FirebaseAuth.getInstance().getUid())){


                if (loginViewModel.loginVMHelper.getCurrentUser().isSeller()){
                    mViewModel.helper.setThisSeller(true);


                    if (mViewModel.helper.getOrderItem() == null ) return;

                    if (mViewModel.helper.getOrderItem().getOrderStatus() == 1){
                        mViewModel.startChangeOrderStatus(oldOrderItem.getId(), OrderStatusUtils.ORDER_STATUS_RECIEVED);
                    }

                }

            }
        });
    }
}
