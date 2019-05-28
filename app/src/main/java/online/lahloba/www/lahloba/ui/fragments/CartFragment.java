package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.internal.Util;
import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.FragmentCartBinding;
import online.lahloba.www.lahloba.ui.adapters.CartAdapter;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddressBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.ExpandableHeightRecyclerView;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;
import online.lahloba.www.lahloba.utils.comparator.CartItemNameComparator;

public class CartFragment extends Fragment {
    private CartViewModel mViewModel;
    FragmentCartBinding binding;
    ExpandableHeightRecyclerView cartRecyclerView;
    LinearLayoutManager linearLayoutManager;
    CartAdapter cartAdapter;
    List<CartItem> cartItemList;
    private Location userLocation;
    AddressesToActivityFromCart addressesToActivityFromCart;
    List<String> marketIds;

    public static CartFragment newInstance(Bundle args, AddressesToActivityFromCart listener) {
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

        mViewModel.cleerMarketPlaceForId();

        View view = binding.getRoot();

        cartRecyclerView = binding.cartItemRecyclerView;
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        cartAdapter = new CartAdapter(getContext());
        cartItemList = new ArrayList<>();
        cartAdapter.setCartItemList(cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /**
         * Observe the addresses to show on address selection
         */
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mViewModel.startGetAddress(FirebaseAuth.getInstance().getUid());

            mViewModel.getAddresses(FirebaseAuth.getInstance().getUid()).observe(this,addresses->{
                addressesToActivityFromCart.onAddressesToActivityFromCart(addresses);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();



        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            cartAdapter.setUserId(FirebaseAuth.getInstance().getUid());
        }

        /**
         * get cart items from firebase if user is logged in
         */
        mViewModel.getCartItems().observe(this,cartItems -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                if (cartItems!=null){
                    /**
                     * Get cart items from firebase
                     */


                    Collections.sort(cartItems, new CartItemNameComparator());


                    cartItemList.clear();
                    cartAdapter.notifyDataSetChanged();

                    cartItemList.addAll(cartItems);
                    cartAdapter.notifyDataSetChanged();

                    calculateTotal();

                    calculateDistance();
                    setHyperlocalData();


                }
            }

        });



        /**
         *  get cart item from internal if user is not loged in
         */
        mViewModel.getCartItemsFromInternal().observe(this,cartItems->{
            if (FirebaseAuth.getInstance().getCurrentUser() == null){
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
                    cartItem.setMarketId(cartChild.getMarketId());
                    cartItemList.add(cartItem);
                }


                cartAdapter.notifyDataSetChanged();

            }
        });


        /**
         *  get Market place of current cart item
         */
        mViewModel.getMarketPlace().observe(this, marketPlace -> {

            if (marketPlace==null)return;

            mViewModel.insertMarketPlaceToInternal(marketPlace);


        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            addressesToActivityFromCart = (AddressesToActivityFromCart) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement AddressesToActivityFromCart");
        }

    }


    void calculateDistance(){

        marketIds = new ArrayList<>();
        for (int i=0; i<cartItemList.size();i++){
            if(marketIds.indexOf(cartItemList.get(i).getMarketId()) > -1){
            }else{
                marketIds.add(cartItemList.get(i).getMarketId());
            }
        }


        for (int i=0; i<marketIds.size();i++){
            mViewModel.startGetMarketPlaceForId(marketIds.get(i));
        }

    }



    public void calculateTotal(){

        double total = 0;

        for (int i=0; i < cartItemList.size(); i++){
            double price = 0;
            price = Double.parseDouble(cartItemList.get(i).getPrice()) * cartItemList.get(i).getCount();
            total = total+price;
        }


        mViewModel.cartVMHelper.setTotal(String.valueOf(total));

    }

    public CartViewModel getmViewModel() {
        return mViewModel;
    }

    public interface AddressesToActivityFromCart{
        void onAddressesToActivityFromCart(List<AddressItem> addresses);
    }


    /**
     *  finish the calculations of hyper local and show in the screen
     */
    public void setHyperlocalData(){

        userLocation = new Location("");
        userLocation.setLatitude(25.6);
        userLocation.setLongitude(32.5);


        mViewModel.getMarketPlaceFromInternal(marketIds)
                .observe(this, marketPlaces -> {


                    if (marketPlaces != null){
                        binding.hyperlocalCostTV.setText("");
                        mViewModel.cartVMHelper.setHyperlocalCost(0);

                        for (MarketPlace marketPlace : marketPlaces){
                            Log.v("string", ""+marketIds.size() +"-"+ marketPlace.getId());


                            Location marketplaceLocation = new Location("");
                            marketplaceLocation.setLatitude(marketPlace.getLat());
                            marketplaceLocation.setLongitude(marketPlace.getLan());
                            double distance = marketplaceLocation.distanceTo(userLocation)/1000;

                            mViewModel.cartVMHelper.setHyperlocalCost(mViewModel.cartVMHelper.getHyperlocalCost() + Utils.getCostByDistance(distance));
                            binding.hyperlocalCostTV.append(marketPlace.getName() + "   "+getString(R.string.cost)+": "+ Utils.getCostByDistance(distance)+"\n");

                        }
                    }
                });
    }


}
