package online.lahloba.www.lahloba.ui.cart;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.databinding.FragmentCartBinding;
import online.lahloba.www.lahloba.ui.adapters.CartAdapter;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.order.OrdersActivity;
import online.lahloba.www.lahloba.utils.ExpandableHeightRecyclerView;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;
import online.lahloba.www.lahloba.utils.comparator.CartItemNameComparator;

public class CartFragment extends Fragment {
    private CartViewModel mViewModel;
    FragmentCartBinding binding;

    CartAdapter cartAdapter;
    List<CartItem> cartItemList;
    private Location userLocation;
    AddressesToActivityFromCart addressesToActivityFromCart;
    List<String> marketIds;
    double nonFinalHyperLocalCost;

    int orderAddedCount = 0;
    int orderInCart = 0;

    LoginViewModel loginViewModel;

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

        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(CartViewModel.class);
        binding.setCartViewModel(mViewModel);

        marketIds = new ArrayList<>();

        loginViewModel.startGetUserDetails(FirebaseAuth.getInstance().getUid());

        mViewModel.cleerMarketPlaceForId();
        mViewModel.resetIsOrderAdded(false);


        /**
         *   Cart RecyclerView
         */

        cartItemList = new ArrayList<>();

        cartAdapter = new CartAdapter(getContext());
        cartAdapter.setCartViewModel(mViewModel);
        cartAdapter.setCartItemList(cartItemList);

        binding.cartItemRecyclerView.setAdapter(cartAdapter);
        binding.cartItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();



        loginViewModel.loginVMHelper.getIsLogged().observe(this,isLogged->{
            if (isLogged==null)return;

            if (isLogged){
                mViewModel.startGetCartItems(FirebaseAuth.getInstance().getUid());
                mViewModel.startGetAddress(FirebaseAuth.getInstance().getUid());
            }
        });



        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            cartAdapter.setUserId(FirebaseAuth.getInstance().getUid());
        }

        /**
         * get cart items from firebase if user is logged in
         */
        mViewModel.getCartItems().observe(this,cartItems -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
            if (cartItems ==null ) return;

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

        });



        /**
         *  get cart item from internal if user is not loged in
         */
        mViewModel.getCartItemsFromInternal().observe(this,cartItems->{
            if (FirebaseAuth.getInstance().getCurrentUser() == null){
                cartItemList.clear();
                cartAdapter.notifyDataSetChanged();

                for (CartItem cartChild : cartItems){
                    cartItemList.add(cartChild);
                }

                calculateTotal();


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


        /**
         * Order Added
         */

        mViewModel.getIsOrderAdded().observe(this, isOrderAdded->{
            if (null == isOrderAdded) return;

            if (isOrderAdded){
                orderAddedCount++;

                if (orderInCart == orderAddedCount && orderInCart != 0){
                    mViewModel.deleteCartItems();
                    getActivity().finish();
                    startActivity(new Intent(getContext(), OrdersActivity.class));
                    Toast.makeText(getContext(), "Order added", Toast.LENGTH_SHORT).show();

                }


            }

        });


        /**
         * get Marketplaces from internal
         */
        mViewModel.getMarketPlacesFromInternal(marketIds)
                .observe(this, marketPlaces -> {
                    if (marketPlaces == null ) return;

                    binding.hyperlocalCostTV.setText("");
                    mViewModel.cartVMHelper.setHyperlocalCost(0);
                    nonFinalHyperLocalCost = 0;

                    List<MarketPlace> uniqueMarketPlaces = new ArrayList<>();



                    for (MarketPlace marketPlace : marketPlaces){

                        for (CartItem cartItem : cartItemList){
                            if (cartItem.getMarketId().equals(marketPlace.getId())){

                                if (!uniqueMarketPlaces.contains(marketPlace)){
                                    uniqueMarketPlaces.add(marketPlace);
                                }

                            }
                        }


                    }

                    for (MarketPlace uniqueMarketPlace : uniqueMarketPlaces){
                        Location marketplaceLocation = new Location("");
                        marketplaceLocation.setLatitude(uniqueMarketPlace.getLat());
                        marketplaceLocation.setLongitude(uniqueMarketPlace.getLan());

                        if(userLocation == null)return;

                        double distance = marketplaceLocation.distanceTo(userLocation)/1000;

                        nonFinalHyperLocalCost = nonFinalHyperLocalCost + Utils.getCostByDistance(distance);

                        if (mViewModel.cartVMHelper.getShippingMethodSelected() != null){
                            mViewModel.cartVMHelper.setHyperlocalCost(nonFinalHyperLocalCost);
                        }

                        binding.hyperlocalCostTV.append(uniqueMarketPlace.getName() + "   "+getString(R.string.cost)+": "+ Utils.getCostByDistance(distance)+"\n");
                    }



                });


    }



    void calculateDistance(){

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


        mViewModel.cartVMHelper.setTotal(total);

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
           if (mViewModel.cartVMHelper.getAddressSelected() != null){

               userLocation = new Location("");
               userLocation.setLatitude(mViewModel.cartVMHelper.getAddressSelected().getLat());
               userLocation.setLongitude(mViewModel.cartVMHelper.getAddressSelected().getLat());
           }

    }


    public void startRetrieveCartItemsAfterSelectAddress(){
        mViewModel.startGetCartItems(FirebaseAuth.getInstance().getUid());
    }

    public double getNonFinalHyperLocalCost() {
        return nonFinalHyperLocalCost;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
