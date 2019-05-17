package online.lahloba.www.lahloba.ui.main;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.ActivityMainBinding;
import online.lahloba.www.lahloba.ui.fragments.AccountFragment;
import online.lahloba.www.lahloba.ui.fragments.FavoriteFragment;
import online.lahloba.www.lahloba.ui.fragments.LoginFragment;
import online.lahloba.www.lahloba.ui.fragments.ShoppingFragment;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    LocationManager mLocationManager;
    Location location;

    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.toolbar.setTitle(getResources().getString(R.string.app_name));
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_my_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewModelProviderFactory factory = Injector.getVMFactory(this, null);
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);

        Fragment fragment = new ShoppingFragment();
        loadFragment(fragment);


//
//        String push = FirebaseDatabase.getInstance().getReference().child("MarketPlace").push().getKey();
//        MarketPlaceItem marketPlaceItem = new MarketPlaceItem();
//        marketPlaceItem.setId(push);
//        marketPlaceItem.setName("اولاد رجب حلوان");
//        marketPlaceItem.setSellerId("sellerId002");
//        marketPlaceItem.setSellerName("اولاد رجب");
//        FirebaseDatabase.getInstance().getReference().child("MarketPlace/"+push).setValue(marketPlaceItem);
//
//        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("MarketPlaceLocation");
//        GeoFire geoFire = new GeoFire(firebaseDatabase);
//        geoFire.setLocation(push, new GeoLocation(29.821747, 31.324114), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                Toast.makeText(MainActivity.this, "one" , Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.ic_action_shopping:
                    fragment = new ShoppingFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.ic_action_favorite:
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.ic_action_my_account:
                    loginAccountFragmetReplace();
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    public void loginAccountFragmetReplace() {
        loginViewModel.getIsLogged().observe(MainActivity.this, isLogged -> {
            if (isLogged) {
                Fragment accountFragment = new AccountFragment();
                loadFragment(accountFragment);
            } else {
                Fragment loginFragment = new LoginFragment();
                loadFragment(loginFragment);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION_REQUEST_CODE);



                }else{
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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

    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
     */
    public List<Address> getGeocoderAddress(Context context, double latitude, double longitude, int maxAddresses ) {
        if (location != null) {

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, maxAddresses);

                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    public String getAddressLine(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getLocality(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            return locality;
        }
        else {
            return null;
        }
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    public String getPostalCode(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        } else {
            return null;
        }
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    public String getCountryName(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();

            return countryName;
        } else {
            return null;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);
        SharedPreferencesManager.setCurrentLocationLat(this, String.valueOf(location.getLatitude()));
        SharedPreferencesManager.setCurrentLocationLan(this, String.valueOf(location.getLongitude()));
        SharedPreferencesManager.setCurrentLocationAddress(this, getAddressLine(this,location.getLatitude(),location.getLongitude(),1));
        Toast.makeText(this, "You Current Location is : "+getAddressLine(this,location.getLatitude(),location.getLongitude(),1), Toast.LENGTH_SHORT).show();
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
