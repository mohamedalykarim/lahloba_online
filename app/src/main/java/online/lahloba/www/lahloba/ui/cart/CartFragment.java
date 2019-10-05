package online.lahloba.www.lahloba.ui.cart;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductOption;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.FragmentCartBinding;
import online.lahloba.www.lahloba.ui.adapters.CartAdapter;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddOrderConfirmBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddressBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.order.OrdersActivity;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.StatusUtils;
import online.lahloba.www.lahloba.utils.Utils;

import static android.app.Activity.RESULT_OK;

public class CartFragment extends Fragment {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private CartViewModel mViewModel;
    private LoginViewModel loginViewModel;
    private FragmentCartBinding binding;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private Location userLocation;
    private List<String> marketIds;
    private double nonFinalHyperLocalCost;

    private int orderAddedCount = 0;
    private int orderInCart = 0;
    private boolean isStartAddingNewOrder;


    private LogginBottomSheet logginBottomSheet;
    private ShippingMethodBottomSheet shippingMethodBottomSheet;
    private AddressBottomSheet addressBottomSheet;
    private AddOrderConfirmBottomSheet addOrderConfirmBottomSheet;








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
         * Google Sign in
         */


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();




        /**
         *   Cart RecyclerView
         */

        cartItemList = new ArrayList<>();

        cartAdapter = new CartAdapter(getContext());
        cartAdapter.setmViewModel(mViewModel);
        cartAdapter.setCartItemList(cartItemList);

        binding.cartItemRecyclerView.setAdapter(cartAdapter);
        binding.cartItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        logginBottomSheet = new LogginBottomSheet();
        shippingMethodBottomSheet = new ShippingMethodBottomSheet();
        addressBottomSheet = new AddressBottomSheet();




        binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (null == FirebaseAuth.getInstance().getCurrentUser()){
                    /*
                     * If not logged in ask user for login
                     */

                    logginBottomSheet.show(getActivity().getSupportFragmentManager(),"logginBottomSheet");
                }else{
                    /*
                     * user is log in
                     */



                    if (mViewModel.cartVMHelper.getAddressSelected() == null){



                        addressBottomSheet.setCartViewModel(mViewModel);
                        addressBottomSheet.show(getActivity().getSupportFragmentManager(),"addressBottomSheet");

                    }else {



                        if (cartItemList.size() < 1){
                            Toast.makeText(getActivity(), "Add products first", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        /*
                         * address selected, choose shipping method
                         */

                        if (mViewModel.cartVMHelper.getShippingMethodSelected() == null){
                            /*
                             * Shipping method not set
                             */

                            shippingMethodBottomSheet.show(getActivity().getSupportFragmentManager(),"shippingMethodBottomSheet");

                        }else{

                            if (cartItemList.size() < 1){
                                Toast.makeText(getActivity(), "Add products first", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            /*
                             * Start the order
                             */



                            addOrderConfirmBottomSheet = new AddOrderConfirmBottomSheet();
                            addOrderConfirmBottomSheet.show(getActivity().getSupportFragmentManager(),"addOrderConfirmBottomSheet");



                        }
                    }






                }
            }
        });



        return binding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getUid() != null){
            cartItemList.clear();
            cartAdapter.notifyDataSetChanged();
            mViewModel.startGetCartItems(FirebaseAuth.getInstance().getUid());
            mViewModel.startGetAddress(FirebaseAuth.getInstance().getUid());
        }



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

            cartItemList.clear();

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



        /*******************************************************************
         *                         ORDER ADDED
         ******************************************************************/

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


    }





    /*******************************************************************
     *               LOG IN
     ******************************************************************/
    public void onLoginSheetItemClicked(int id) {
        if (id == R.id.loginBtn){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            logginBottomSheet.dismiss();
        }else if (id == R.id.googleLoginBtn){
            signIn();

            logginBottomSheet.dismiss();
        }
    }






    /*******************************************************************
     *               SELECT ADDRESS
     ******************************************************************/
    public void onSendAddressToCart(AddressItem addressItems) {
        mViewModel.cartVMHelper.setAddressSelected(addressItems);

        mViewModel.startGetCartItems(FirebaseAuth.getInstance().getUid());

        if (addressBottomSheet !=null)
            addressBottomSheet.dismiss();



        /**
         *   Choose hyperlocal automatically
         */

        mViewModel.cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

        mViewModel.cartVMHelper.setHyperlocalCost(nonFinalHyperLocalCost);

    }






    /*******************************************************************
     *                      SHIPPING METHOD
     ******************************************************************/
    public void onShippingMethodClicked(int id) {
        if (id == R.id.freeShippingBtn){
            mViewModel.cartVMHelper.setShippingMethodSelected(CartVMHelper.FREE_SHIPPING);
            shippingMethodBottomSheet.dismiss();
        }else if (id == R.id.hyperLocalShippingBtn3){

            mViewModel.cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

            mViewModel.cartVMHelper.setHyperlocalCost(nonFinalHyperLocalCost);

            shippingMethodBottomSheet.dismiss();
        }
    }





    /******************************************************************
     *               HYPERLOCAL
     ******************************************************************/

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





    /*******************************************************************
     *
     *               TOTAL
     *
     ******************************************************************/

    public void calculateTotal(){

        double total = 0;
        double price = 0;

        for (int i=0; i < cartItemList.size(); i++){
            price = Double.parseDouble(cartItemList.get(i).getPrice());

            if (cartItemList.get(i).getOptions() != null){
                HashMap<String, ProductOption> optionHashMap = cartItemList.get(i).getOptions();
                double optionPrice = 0;
                for (ProductOption option : optionHashMap.values()){
                    optionPrice += Double.valueOf(option.getOptionValue());
                }

                price = price + optionPrice;

            }

            price = price * cartItemList.get(i).getCount();



        }

        total = total+price;



        mViewModel.cartVMHelper.setTotal(total);

    }







    /*******************************************************************
     *
     *               confirm adding THE ORDER FROM THE CURRENT CART
     *
     ******************************************************************/


    public void onClickConfirmItem(int id) {

        if (id== R.id.confirmBtn){
            /**
             * Confirm adding order
             */


            orderAddedCount = 0;

            if (!isStartAddingNewOrder){
                if (cartItemList.size() < 1){
                    Toast.makeText(getActivity(), "Add products first", Toast.LENGTH_SHORT).show();
                    return;
                }


                List<String> marketIds = new ArrayList<>();

                for (int i=0; i<cartItemList.size();i++){
                    if(marketIds.indexOf(cartItemList.get(i).getMarketId()) > -1){
                    }else{
                        marketIds.add(cartItemList.get(i).getMarketId());
                    }

                }

                orderInCart = marketIds.size();




                for (String marketId : marketIds){

                    OrderItem orderItem = new OrderItem();
                    HashMap<String, CartItem> cartItems = new HashMap<>();
                    for (CartItem item : cartItemList){
                        if (item.getMarketId().equals(marketId)){
                            cartItems.put(item.getId(), item);
                        }
                    }

                    orderItem.setProducts(cartItems);


                    Injector.getExecuter().diskIO().execute(()->{

                        MarketPlace marketPlace = mViewModel.getMarketPlaceFromInternal(marketId);

                        if (marketPlace == null)return;


                        Location userLocation = new Location("");
                        userLocation.setLongitude(mViewModel.cartVMHelper.getAddressSelected().getLat());
                        userLocation.setLatitude(mViewModel.cartVMHelper.getAddressSelected().getLat());

                        Location marketplaceLocation = new Location("");
                        marketplaceLocation.setLatitude(marketPlace.getLat());
                        marketplaceLocation.setLongitude(marketPlace.getLan());
                        double distance = marketplaceLocation.distanceTo(userLocation)/1000;
                        double hyperlocalCost = Utils.getCostByDistance(distance);
                        orderItem.setHyperlocalCost(hyperlocalCost);

                        double total  = 0;
                        double price = 0;

                        for(CartItem cartItem: cartItems.values()){
                            if (cartItem==null)return;
                            price += Double.parseDouble(cartItem.getPrice());

                            if (cartItem.getOptions() != null){
                                HashMap<String, ProductOption> optionHashMap = cartItem.getOptions();

                                double optionPrice = 0;
                                for (ProductOption option : optionHashMap.values()){
                                    optionPrice += Double.valueOf(option.getOptionValue());
                                }

                                price = price + optionPrice;


                            }

                            price = price * cartItem.getCount();
                            total += price;
                        }

                        orderItem.setTotal(total);
                        orderItem.setOrderTotal(
                                orderItem.getHyperlocalCost()
                                        +orderItem.getTotal()
                        );

                        orderItem.setMarketplaceId(marketId);
                        orderItem.setUserId(FirebaseAuth.getInstance().getUid());

                        orderItem.setOrderStatus(StatusUtils.ORDER_STATUS_PENDING);
                        orderItem.setCityId(marketPlace.getAddressSelected().getCityId());
                        orderItem.setCityIdStatus(marketPlace.getAddressSelected().getCityId()+"-"+ StatusUtils.ORDER_STATUS_PENDING);







                        mViewModel.startNewOrder(orderItem);



                    });

                }

                isStartAddingNewOrder = true;






            }



        }else if (id == R.id.cancelBtn){
            addOrderConfirmBottomSheet.dismiss();
        }
    }







    /*******************************************************************
     *               GOOGLE LOG IN
     ******************************************************************/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);



                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    // [START_EXCLUDE]
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            cartAdapter.setUserId(user.getUid());

                            loginViewModel.loginVMHelper.setLogged(true);


                            String familyName = acct.getFamilyName();
                            String firstName = user.getDisplayName().replace(familyName,"");

                            UserItem userItem = new UserItem();
                            userItem.setEmail(user.getEmail());
                            userItem.setFirstName(firstName);
                            userItem.setLastName(familyName);
                            userItem.setId(FirebaseAuth.getInstance().getUid());
                            userItem.setSeller(false);
                            userItem.setStatus(true);
                            userItem.setDelivery(false);
                            userItem.setDeliverySupervisor(false);

                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            mRef.child("User").child(FirebaseAuth.getInstance().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()){
                                                mRef.child("User").child(userItem.getId()).setValue(userItem);
                                                loginViewModel.startUpdateMessagingToken();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }







    /*******************************************************************
     *
     *               Setter
     *
     ******************************************************************/

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

}
