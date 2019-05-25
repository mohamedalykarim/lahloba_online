package online.lahloba.www.lahloba.ui.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.Country;
import online.lahloba.www.lahloba.data.model.Governerate;
import online.lahloba.www.lahloba.databinding.FragmentGovernerateBinding;
import online.lahloba.www.lahloba.ui.adapters.GovernerateAdapter;
import online.lahloba.www.lahloba.ui.governerate.GovernerateViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;


public class GovernerateFragment extends Fragment implements GovernerateAdapter.OnChildClicked , LocationListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;

    private GovernerateViewModel mViewModel;
    RecyclerView governorateRV;
    FragmentGovernerateBinding binding;
    LocationManager mLocationManager;
    Location location;

    public static GovernerateFragment newInstance() {
        return new GovernerateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGovernerateBinding.inflate(inflater,container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this, factory).get(GovernerateViewModel.class);

        mViewModel.startGetGovernorates();

        governorateRV = binding.governorateRV;
        governorateRV.setLayoutManager(new LinearLayoutManager(getContext()));


        List<Country> countries = new ArrayList<>();

        if (null == SharedPreferencesManager.getCurrentLocationAddress(getContext()) || SharedPreferencesManager.getCurrentLocationAddress(getContext()).equals("")){
            binding.currentLocationContainer.setVisibility(View.INVISIBLE);
        }else{
            binding.currentLocation.setText(SharedPreferencesManager.getCurrentLocationAddress(getContext()));
        }

        mViewModel.getGovernorates().observe(this,governorates->{
            countries.clear();

            for (DataSnapshot child: governorates.getChildren()) {
                List<Governerate> egyptGovernorate = new ArrayList<>();

                String name = child.child("name").getValue().toString();
                DataSnapshot governorate = child.child("governorate");


                for (DataSnapshot governorateChild : governorate.getChildren()) {
                    egyptGovernorate.add(governorateChild.getValue(Governerate.class));
                }

                Country country = new Country(name, egyptGovernorate);
                countries.add(country);


            }

            GovernerateAdapter governerateAdapter = new GovernerateAdapter(countries, this);
            governorateRV.setAdapter(governerateAdapter);

        });

        binding.getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION_REQUEST_CODE);


                } else {
                    getCurrentLocation();
                }


            }
        });







        return binding.getRoot();
    }


    @Override
    public void onChildClicked(String currentLocation) {
        binding.currentLocation.setText(currentLocation);
        binding.currentLocationContainer.setVisibility(View.VISIBLE);
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
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
     * Try to get Locality
     * @return null or locality
     */
    public String getPremises(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getPremises();

            return locality;
        }
        else {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getSubAdminArea(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getSubAdminArea();

            return locality;
        }
        else {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getAdminArea(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getAdminArea();

            return locality;
        }
        else {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getSubLocality(Context context, double latitude, double longitude, int maxAddresses) {
        List<Address> addresses = getGeocoderAddress(context, latitude, longitude, maxAddresses);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getSubLocality();

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
        if (getAddressLine(getContext(),location.getLatitude(),location.getLongitude(),1) != null){
            SharedPreferencesManager.setCurrentLocationLat(getContext(), String.valueOf(location.getLatitude()));
            SharedPreferencesManager.setCurrentLocationLan(getContext(), String.valueOf(location.getLongitude()));

            String cityName = ""+getAdminArea(getContext(),location.getLatitude(),location.getLongitude(),1);

            if (cityName.equals("null")){
                cityName = ""+getSubAdminArea(getContext(),location.getLatitude(),location.getLongitude(),1);
            }

            SharedPreferencesManager.setCurrentLocationAddress(getContext(), cityName);

            binding.currentLocation.setText(SharedPreferencesManager.getCurrentLocationAddress(getContext()));

        }else{
            onLocationChanged(location);
        }


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
