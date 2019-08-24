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
    private OrderItem orderItem;

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



        Intent oldIntent = getActivity().getIntent();
        if (oldIntent == null || !oldIntent.hasExtra(ORDER_ITEM)){
            getActivity().finish();
        }

        orderItem = oldIntent.getParcelableExtra(ORDER_ITEM);

        mViewModel.startGetUserForOrder(orderItem.getUserId());



        binding.setOrder(orderItem);


        /*
         * Add Products to view
         */
        HashMap<String, CartItem> products = orderItem.getProducts();

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
                mViewModel.startReorder(orderItem);
            }
        });




        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        loginViewModel.getCurrentUserDetails().observe(this, userItem -> {
            if (userItem == null) return;
            if (userItem.isSeller()){

                if (orderItem.getOrderStatus() == 1){
                    mViewModel.startChangeOrderStatus(orderItem.getId(), OrderStatusUtils.ORDER_STATUS_RECIEVED);
                }
            }

            loginViewModel.loginVMHelper.setCurrentUser(userItem);

        });

        mViewModel.getUserForOrder().observe(this, userItem -> {
            if (userItem == null) return;

            Log.v("sss", userItem.getFirstName()+" "+userItem.getFirstName() );

            binding.setUserForOrder(userItem);

        });
    }
}
