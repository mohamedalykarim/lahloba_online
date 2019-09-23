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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductOption;
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
import online.lahloba.www.lahloba.utils.StatusUtils;
import online.lahloba.www.lahloba.utils.Utils;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements
        LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked,
        AddressBottomSheet.SendAddressToCart,
        AddOrderConfirmBottomSheet.ConfirmClickListener

{


    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);


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


            }



        }


    }



    /**
     * Login sheet Item clicked
     * @param id the id of button clicked (login method chosen)
     */

    @Override
    public void onLoginSheetItemClicked(int id) {
        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .onLoginSheetItemClicked(id);
    }


    /**
     * Shipping sheet item clicked
     * @param id
     */

    @Override
    public void onShippingMethodClicked(int id) {

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .onShippingMethodClicked(id);
    }







    /**
     * Get selected Address From Adapter to activity
     */
    @Override
    public void onSendAddressToCart(AddressItem addressItems) {
        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .onSendAddressToCart(addressItems);
    }



    @Override
    public void onClickConfirmItem(int id) {

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .onClickConfirmItem(id);

    }







}
