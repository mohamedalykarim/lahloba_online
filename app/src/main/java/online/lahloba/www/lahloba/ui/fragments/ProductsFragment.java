package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.databinding.FragmentProductBinding;
import online.lahloba.www.lahloba.ui.adapters.ProductAdapter;
import online.lahloba.www.lahloba.ui.cart.CartActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;

public class ProductsFragment extends Fragment {
    FragmentProductBinding binding;
    private ProductsViewModel mViewModel;
    private LoginViewModel loginViewModel;
    List<ProductItem> productItemList;
    ProductAdapter productAdapter;
    RecyclerView productsRV;
    String userId;



    public static ProductsFragment newInstance(Bundle args) {
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_product, container, false);
        binding.setLifecycleOwner(this);

        View view = binding.getRoot();
        productItemList = new ArrayList<>();
        productsRV = view.findViewById(R.id.productsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        productAdapter = new ProductAdapter(getContext());
        productAdapter.setProductItemList(productItemList);
        productsRV.setLayoutManager(linearLayoutManager);
        productsRV.setAdapter(productAdapter);




        Bundle bundle = getArguments();
        String subId = bundle.getString(Constants.EXTRA_SUBTITLE_ID);

        VMPHelper vmpHelper = new VMPHelper();
        vmpHelper.setCategoryId(subId);

        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(), vmpHelper);
        mViewModel = ViewModelProviders.of(this, factory).get(ProductsViewModel.class);

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext(),null);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);


        binding.setProductViewModel(mViewModel);

        mViewModel.deleteAllFromCartCount0();


        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            mViewModel.getCartItem().observe(this, cartItems -> {
                mViewModel.productVMHelper.setCartCount(cartItems.size());
            });
        }else{
            mViewModel.getCartItemFromInternal().observe(this, cartItems->{
                mViewModel.productVMHelper.setCartCount(cartItems.size());
            });
        }



        mViewModel.getProductsForCategory().observe(this, products->{
            productItemList.clear();
            productItemList.addAll(products);
            productAdapter.notifyDataSetChanged();

        });

        floatButton(container);





        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.startProductsForCategory();

        productAdapter = new ProductAdapter(getContext());
        productItemList = new ArrayList<>();
        productAdapter.setProductItemList(productItemList);
        productsRV.setAdapter(productAdapter);

        loginViewModel.getCurrentUserDetails().observe(this,currentUser->{
            if (null != currentUser){
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    userId = currentUser.getId();
                    productAdapter.setUserId(userId);
                    mViewModel.startGetCartItems(userId);
                }

            }
        });
    }

    public void floatButton(View container){
        binding.floatingToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), CartActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID,userId);
                container.getContext().startActivity(intent);
            }
        });
    }
}
