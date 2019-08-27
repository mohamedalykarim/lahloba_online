package online.lahloba.www.lahloba.ui.cart;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddOrderConfirmBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddressBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.OrderStatusUtils;
import online.lahloba.www.lahloba.utils.Utils;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements
        LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked,
        AddressBottomSheet.SendAddressToCart,
        AddOrderConfirmBottomSheet.ConfirmClickListener

{

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;



    LogginBottomSheet logginBottomSheet;
    ShippingMethodBottomSheet shippingMethodBottomSheet;
    AddressBottomSheet addressBottomSheet;
    AddOrderConfirmBottomSheet addOrderConfirmBottomSheet;

    private LoginViewModel loginViewModel;
    private boolean isStartAddingNewOrder;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);

        logginBottomSheet = new LogginBottomSheet();
        shippingMethodBottomSheet = new ShippingMethodBottomSheet();
        addressBottomSheet = new AddressBottomSheet();


        /**
         * Google Sign in
         */


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        /***
         * Login View Model in cart activity
         */

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);





        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));
                CartFragment cartFragment = CartFragment.newInstance(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, cartFragment)
                        .commitNow();

                ((CartFragment)getSupportFragmentManager().getFragments().get(0)).setLoginViewModel(loginViewModel);


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            /**
                             * If not logged in ask user for login
                             */
                            
                            logginBottomSheet.show(getSupportFragmentManager(),"");
                        }else{
                            /**
                             * user is log in
                             */



                            if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                    .getmViewModel().cartVMHelper.getAddressSelected() == null){

                                addressBottomSheet.setCartViewModel(((CartFragment)((CartFragment) getSupportFragmentManager().getFragments().get(0))).getmViewModel());
                                addressBottomSheet.show(getSupportFragmentManager(),"");

                            }else {

                                if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                                    Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                  /**
                                   * address selected then choose shipping method
                                   */

                                if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                        .getmViewModel().cartVMHelper.getShippingMethodSelected() == null){
                                    /**
                                     * Shipping method not set
                                     */

                                    shippingMethodBottomSheet.show(getSupportFragmentManager(),"");

                                }else{

                                    if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                                        Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    /*
                                     * Start the order
                                     */

                                    addOrderConfirmBottomSheet = new AddOrderConfirmBottomSheet();
                                    addOrderConfirmBottomSheet.show(getSupportFragmentManager(),"");

                                }
                            }



                            
                            

                        }
                    }
                });
            }



        }
    }



    /**
     * Login sheet Item clicked
     * @param id the id of button clicked (login method chosen)
     */

    @Override
    public void onLoginSheetItemClicked(int id) {
        if (id == R.id.loginBtn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            logginBottomSheet.dismiss();
        }else if (id == R.id.googleLoginBtn){
            signIn();

            logginBottomSheet.dismiss();
        }
    }


    /**
     * Shipping sheet item clicked
     * @param id
     */

    @Override
    public void onShippingMethodClicked(int id) {
        if (id == R.id.freeShippingBtn){
            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel()
                    .cartVMHelper.setShippingMethodSelected(CartVMHelper.FREE_SHIPPING);
            shippingMethodBottomSheet.dismiss();
        }else if (id == R.id.hyperLocalShippingBtn3){

            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel().cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel().cartVMHelper
                    .setHyperlocalCost(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getNonFinalHyperLocalCost());

            shippingMethodBottomSheet.dismiss();
        }
    }







    /**
     * Get selected Address From Adapter to activity
     */
    @Override
    public void onSendAddressToCart(AddressItem addressItems) {
        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper.setAddressSelected(addressItems);

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .startRetrieveCartItemsAfterSelectAddress();

        if (addressBottomSheet !=null)
            addressBottomSheet.dismiss();



        /**
         *   Choose hyperlocal automatically
         *
         *
         */

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper
                .setHyperlocalCost(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getNonFinalHyperLocalCost());


    }



    @Override
    public void onClickConfirmItem(int id) {
        if (id== R.id.confirmBtn){
            /**
             * Confirm adding order
             */

            ((CartFragment)getSupportFragmentManager().getFragments().get(0)).orderAddedCount = 0;

            if (!isStartAddingNewOrder){
                if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                    Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                    return;
                }


                List<String> marketIds = new ArrayList<>();

                for (int i=0; i<((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size();i++){
                    if(marketIds.indexOf(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().get(i).getMarketId()) > -1){
                    }else{
                        marketIds.add(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().get(i).getMarketId());
                    }

                }

                ((CartFragment)getSupportFragmentManager().getFragments().get(0)).orderInCart = marketIds.size();


                for (String marketId : marketIds){

                    OrderItem orderItem = new OrderItem();
                    HashMap<String, CartItem> products = new HashMap<>();
                    for (CartItem item : ((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList()){
                        if (item.getMarketId().equals(marketId)){
                            products.put(item.getId(), item);
                        }
                    }

                    orderItem.setProducts(products);
                    orderItem.setAddressSelected(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getAddressSelected());
                    orderItem.setPay_method(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getPay_method());
                    orderItem.setShippingMethodSelected(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getShippingMethodSelected());


                    ((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel()
                            .getMarketPlaceFromInternal(marketId).observe(this, marketPlace->{
                                if (marketPlace == null)return;

                        Location userLocation = new Location("");
                        userLocation.setLatitude(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel()
                                .cartVMHelper.getAddressSelected().getLat());
                        userLocation.setLongitude(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel()
                                .cartVMHelper.getAddressSelected().getLat());

                        Location marketplaceLocation = new Location("");
                        marketplaceLocation.setLatitude(marketPlace.getLat());
                        marketplaceLocation.setLongitude(marketPlace.getLan());
                        double distance = marketplaceLocation.distanceTo(userLocation)/1000;
                        double hyperlocalCost = Utils.getCostByDistance(distance);
                        orderItem.setHyperlocalCost(hyperlocalCost);

                        double total  = 0;

                        for(CartItem product: products.values()){
                            if (product!=null){
                                total+= Double.parseDouble(product.getPrice());
                            }
                        }

                        orderItem.setTotal(total);
                        orderItem.setOrderTotal(
                                orderItem.getHyperlocalCost()
                                        +orderItem.getTotal()
                        );

                        orderItem.setMarketplaceId(marketId);
                        orderItem.setUserId(FirebaseAuth.getInstance().getUid());

                        orderItem.setOrderStatus(OrderStatusUtils.ORDER_STATUS_PENDING);

                        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                .getmViewModel()
                                .startNewOrder(orderItem);


                    });




                }

                isStartAddingNewOrder = true;






            }



        }else if (id == R.id.cancelBtn){
            addOrderConfirmBottomSheet.dismiss();
        }
    }


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
                    Log.w("mmm", "Google sign in failed", e);
                    // [START_EXCLUDE]
                }
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                    .cartAdapter.setUserId(user.getUid());

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
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                            Toast.makeText(CartActivity.this, ""+loginViewModel.loginVMHelper.getIsLogged().getValue(), Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }


}
