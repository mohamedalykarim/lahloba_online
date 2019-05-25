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

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.fragments.CartFragment;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements LocationListener,
        LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked {

    LogginBottomSheet logginBottomSheet;
    ShippingMethodBottomSheet shippingMethodBottomSheet;

    private CartViewModel mViewModel;
    private LoginViewModel loginViewModel;
    private UserItem userItem;

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    LocationManager mLocationManager;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this,null);
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


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            /**
                             * If not login ask user for login
                             */
                            
                            logginBottomSheet = new LogginBottomSheet();
                            logginBottomSheet.show(getSupportFragmentManager(),"");
                        }else{
                            /**
                             * user is log in
                             */


                            if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                    .getmViewModel().cartVMHelper.getShippingMethodSelected() == null){
                                /**
                                 * Shipping method not set
                                 */

                                shippingMethodBottomSheet = new ShippingMethodBottomSheet();
                                shippingMethodBottomSheet.show(getSupportFragmentManager(),"");

                            }else{
                                /**
                                 * Shipping method is set
                                 */


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
        
        loginViewModel.getCurrentUserDetails().observe(this, userItem -> {
            this.userItem = userItem;
        });
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

            if (userItem != null){
                if (userItem.getLat() == 0 || userItem.getLan() == 0){
                    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                ACCESS_FINE_LOCATION_REQUEST_CODE);


                    } else {
                        getCurrentLocation();
                    }


                }else{
                    Location currentLocation = new Location("");
                    currentLocation.setLatitude(userItem.getLat());
                    currentLocation.setLongitude(userItem.getLan());

                    //todo

                }
            }


            shippingMethodBottomSheet.dismiss();
        }
    }


    private void getCurrentLocation() {
        boolean gps_enabled = false;

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }


        if (!gps_enabled) {
            // notify user
            buildAlertMessageNoGps();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this,null);
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
