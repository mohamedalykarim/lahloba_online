package online.lahloba.www.lahloba.ui.city;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.Governorate;
import online.lahloba.www.lahloba.data.model.GovernorateItem;
import online.lahloba.www.lahloba.databinding.FragmentCityBinding;
import online.lahloba.www.lahloba.ui.adapters.CityAdapter;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;


public class CityFragment extends Fragment implements CityAdapter.OnChildClicked , LocationListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;

    private CityViewModel mViewModel;
    RecyclerView cityRV;
    FragmentCityBinding binding;
    LocationManager mLocationManager;
    Location location;
    private List<GovernorateItem> governorates;

    public static CityFragment newInstance() {
        return new CityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCityBinding.inflate(inflater,container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(CityViewModel.class);

        mViewModel.startGetGovernorate();
        mViewModel.startGetCities();

        cityRV = binding.cityRV;
        cityRV.setLayoutManager(new LinearLayoutManager(getContext()));


        List<Governorate> governorateArrayList = new ArrayList<>();

        if (null == SharedPreferencesManager.getCurrentLocationAddress(getContext()) || SharedPreferencesManager.getCurrentLocationAddress(getContext()).equals("")){
            binding.currentLocationTv.setVisibility(View.INVISIBLE);
        }else{
            binding.currentLocationTv.setText(SharedPreferencesManager.getCurrentLocationAddress(getContext()));
        }

        mViewModel.getGovernorates().observe(this, governorateItems -> {
            if (governorateItems == null) return;
            this.governorates = governorateItems;
        });

        mViewModel.getCities().observe(this,cities->{
            if (cities== null)return;
            if (governorates == null)return;
            governorateArrayList.clear();

            for (GovernorateItem governorateItem :governorates){
                ArrayList<CityItem> filteredCities = new ArrayList<>();

                for (CityItem cityItem : cities){
                    if (cityItem.getParent().equals(governorateItem.getId())){
                        filteredCities.add(cityItem);
                    }
                }


                Governorate governorate = new Governorate(governorateItem.getName(), filteredCities);
                governorateArrayList.add(governorate);



                CityAdapter cityAdapter = new CityAdapter(governorateArrayList, this);
                cityRV.setAdapter(cityAdapter);
            }



        });



        binding.getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mLocationManager != null)
                mLocationManager.removeUpdates(CityFragment.this);


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
        binding.currentLocationTv.setText(currentLocation);
        binding.currentLocationTv.setVisibility(View.VISIBLE);
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

            String adminArea = ""+getAdminArea(getContext(),location.getLatitude(),location.getLongitude(),1);

            String subAdminArea = ""+getSubAdminArea(getContext(),location.getLatitude(),location.getLongitude(),1);

            String cityName;

            if (adminArea.equals("")){
                cityName = subAdminArea;
            } else if (!adminArea.equals("") && !subAdminArea.equals("") ){
                cityName = subAdminArea;
            }else {
                cityName = adminArea;
            }

            SharedPreferencesManager.setCurrentLocationAddress(getContext(), cityName);

            binding.currentLocationTv.setText(SharedPreferencesManager.getCurrentLocationAddress(getContext()));

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
