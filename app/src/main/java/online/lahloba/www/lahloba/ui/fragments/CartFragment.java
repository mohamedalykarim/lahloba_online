package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.databinding.FragmentCartBinding;
import online.lahloba.www.lahloba.ui.adapters.CartAdapter;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.ExpandableHeightRecyclerView;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.comparator.CartItemNameComparator;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    FragmentCartBinding binding;
    ExpandableHeightRecyclerView cartRecyclerView;
    LinearLayoutManager linearLayoutManager;
    CartAdapter cartAdapter;
    List<CartItem> cartItemList;

    public static CartFragment newInstance(Bundle args) {
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false);
        binding.setLifecycleOwner(this);

        VMPHelper vmpHelper = new VMPHelper();
        vmpHelper.setUserId(getArguments().getString(Constants.EXTRA_USER_ID));
        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(), vmpHelper);
        mViewModel = ViewModelProviders.of(this, factory).get(CartViewModel.class);
        binding.setCartViewModel(mViewModel);

        View view = binding.getRoot();

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        cartRecyclerView = binding.cartItemRecyclerView;
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        cartAdapter = new CartAdapter(getContext());
        cartItemList = new ArrayList<>();
        cartAdapter.setCartItemList(cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(linearLayoutManager);


        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            cartAdapter.setUserId(FirebaseAuth.getInstance().getUid());
            mViewModel.startGetCartItems();


            mViewModel.getCartItems().observe(this,cartItems -> {
                if (cartItems==null){
                    mViewModel.startGetCartItems();

                }else {
                    Collections.sort(cartItems, new CartItemNameComparator());

                    calculateTotal();

                    cartItemList.clear();
                    cartAdapter.notifyDataSetChanged();

                    cartItemList.addAll(cartItems);
                    cartAdapter.notifyDataSetChanged();

                }
            });
        }else{
            mViewModel.getCartItemsFromInternal().observe(this,cartItems->{
                double total = 0;
                for (int i=0; i < cartItems.size(); i++){
                    total += Double.parseDouble(cartItems.get(i).getPrice()) * cartItems.get(0).getCount();
                    mViewModel.cartVMHelper.setTotal(String.valueOf(total));
                }

                cartItemList.clear();
                cartAdapter.notifyDataSetChanged();

                for (CartItemRoom cartChild : cartItems){
                    CartItem cartItem = new CartItem();
                    cartItem.setCount(cartChild.getCount());
                    cartItem.setImage(cartChild.getImage());
                    cartItem.setPrice(cartChild.getPrice());
                    cartItem.setProductId(cartChild.getProductId());
                    cartItem.setProductName(cartChild.getProductName());
                    cartItem.setCurrency(cartChild.getCurrency());
                    cartItemList.add(cartItem);
                }


                cartAdapter.notifyDataSetChanged();

            });
        }


    }

    public void calculateTotal(){
        double total = 0;

        for (int i=0; i < cartItemList.size(); i++){
            double price = Double.parseDouble(cartItemList.get(i).getPrice()) * cartItemList.get(0).getCount();
            total = total+price;
        }

        mViewModel.cartVMHelper.setTotal(String.valueOf(total));

    }
}
