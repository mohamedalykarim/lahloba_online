package online.lahloba.www.lahloba.ui.cart;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddressBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.fragments.CartFragment;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements
        LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked,
        CartFragment.AddressesToActivityFromCart,
        AddressBottomSheet.SendAddressToCart

{

    LogginBottomSheet logginBottomSheet;
    ShippingMethodBottomSheet shippingMethodBottomSheet;
    AddressBottomSheet addressBottomSheet;

    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);

        logginBottomSheet = new LogginBottomSheet();
        shippingMethodBottomSheet = new ShippingMethodBottomSheet();
        addressBottomSheet = new AddressBottomSheet();


        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this,null);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);


        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));
                CartFragment cartFragment = CartFragment.newInstance(bundle, this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, cartFragment)
                        .commitNow();


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            /**
                             * If not login ask user for login
                             */
                            
                            logginBottomSheet.show(getSupportFragmentManager(),"");
                        }else{
                            /**
                             * user is log in
                             */

                            if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                    .getmViewModel().cartVMHelper.getAddressSelected() == null){

                                addressBottomSheet.show(getSupportFragmentManager(),"");

                            }else {
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
                                    /**
                                     * Shipping method is set
                                     */


                                }
                            }



                            
                            

                        }
                    }
                });
            }



        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onLoginSheetItemClicked(int id) {
        if (id == R.id.loginBtn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            logginBottomSheet.dismiss();
        }
    }

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



    @Override
    public void onAddressesToActivityFromCart(List<AddressItem> addresses) {
        if (addresses!=null){
            addressBottomSheet.setAddresses(addresses);
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
    }
}
