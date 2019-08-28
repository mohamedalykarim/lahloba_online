package online.lahloba.www.lahloba.ui.address;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.FragmentAddAddressBinding;
import online.lahloba.www.lahloba.ui.adapters.CustomSpinnerAdapter;
import online.lahloba.www.lahloba.utils.Injector;

public class AddAddressFragment extends Fragment implements LocationListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;


    private AddAddressViewModel addAddressViewModel;
    FragmentAddAddressBinding binding;
    private int error = 0;
    private AddressViewModel addressViewModel;
    private AddressItem defaultAddress;

    List<String> citiesIds, governoratesIds;

    double lat, lon;
    String locationAddress;

    LocationManager mLocationManager;
    Location location;

    public static AddAddressFragment newInstance() {
        return new AddAddressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_address,
                container,
                false
        );

        ViewModelProviderFactory addressFactory = Injector.getVMFactory(getContext());
        addressViewModel = ViewModelProviders.of(this,addressFactory).get(AddressViewModel.class);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        addAddressViewModel = ViewModelProviders.of(this,factory).get(AddAddressViewModel.class);


        addAddressViewModel.startGetCitities();
        addAddressViewModel.startGetGovernorates();

        citiesIds = new ArrayList<>();
        governoratesIds = new ArrayList<>();

        addAddressViewModel.setIsAddressAddedFalse();


        binding.addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
                if (error==0){
                    AddressItem addressItem = new AddressItem();
                    addressItem.setDefaultAddress(binding.switch1.isChecked());
                    addressItem.setName(binding.nameET.getText().toString());
                    addressItem.setCountry(binding.countryET.getText().toString());
                    addressItem.setCity(binding.citySpinner.getSelectedItem().toString());
                    addressItem.setCityId(citiesIds.get(binding.citySpinner.getSelectedItemPosition()));
                    addressItem.setGovernorate(binding.governorateSpinner.getSelectedItem().toString());
                    addressItem.setZone(binding.zoneET.getText().toString());
                    addressItem.setStreet(binding.streetET.getText().toString());
                    addressItem.setBuilding(binding.buildingET.getText().toString());
                    addressItem.setFloor(Integer.parseInt(binding.floorET.getText().toString()));
                    addressItem.setFlatNumber(Integer.parseInt(binding.flatNumberET.getText().toString()));
                    addressItem.setLat(lat);
                    addressItem.setLon(lon);
                    addressItem.setAddress(locationAddress);


                    addAddressViewModel.startAddNewAddress(
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            addressItem
                    );



                }
            }
        });

        addAddressViewModel.getIsAddressAdded().observe(this, isAddressAdded->{
            if (isAddressAdded){
                Toast.makeText(getContext(), "New Address Added Successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });


        View view = binding.getRoot();
        return view;
    }




    public void validateForm(){
        error = 0;

        if (binding.nameET.getText().length() < 1){
            binding.nameET.setError("Please Insert The Name Of Address");
            error++;
        }

        if (binding.countryET.getText().length() < 1){
            binding.countryET.setError("Please Insert Governorate Name");
            error++;
        }

        if (binding.zoneET.getText().length() < 1){
            binding.zoneET.setError("Please Insert Zone Name");
            error++;
        }

        if (binding.streetET.getText().length() < 1){
            binding.streetET.setError("Please Insert Street Name");
            error++;
        }

        if (binding.buildingET.getText().length() < 1){
            binding.buildingET.setError("Please Insert Building Name Or Number");
            error++;
        }

        if (binding.floorET.getText().length() < 1){
            binding.floorET.setError("Please Insert Floor Number");
            error++;
        }

        if (binding.flatNumberET.getText().length() < 1){
            binding.flatNumberET.setError("Please Insert Flat Number");
            error++;
        }

        if (lat == 0 || lon == 0){
            Toast.makeText(getContext(), "Please select current location", Toast.LENGTH_SHORT).show();
            error++;
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        if (null == FirebaseAuth.getInstance()){
            getActivity().finish();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST_CODE);


        } else {
            getCurrentLocation();
        }

        addressViewModel.getDefaultAddress().observe(this, defaultAddress->{
            this.defaultAddress = defaultAddress;
        });


        addAddressViewModel.getCities().observe(this, cityItems -> {
            if (cityItems == null)return;
            citiesIds.clear();

            ArrayList<String> citiesNames = new ArrayList<>();

            for (int i = 0; i < cityItems.size(); i++)
            {
                citiesNames.add(cityItems.get(i).getName());
                citiesIds.add(cityItems.get(i).getId());
            }

            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item,citiesNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.citySpinner.setAdapter(adapter);
        });


        addAddressViewModel.getGovernorates().observe(this, governorateItems -> {
            if (governorateItems == null)return;
            governoratesIds.clear();

            ArrayList<String> governoratesNames = new ArrayList<>();

            for (int i = 0; i < governorateItems.size(); i++)
            {
                governoratesNames.add(governorateItems.get(i).getName());
                governoratesIds.add(governorateItems.get(i).getId());
            }

            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item,governoratesNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.governorateSpinner.setAdapter(adapter);
        });

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
        builder.setMessage("Your GPS seems to be disabled to add new address you want to enable it!")
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
        if (location != null){
            lat = location.getLatitude();
            lon = location.getLongitude();

            String address = getAddressLine(getContext(),location.getLatitude(),location.getLongitude(),1);

            if (address == null) {
                address = getAddressLine(getContext(),location.getLatitude(),location.getLongitude(),1);
                if (address == null){
                    address = getAddressLine(getContext(),location.getLatitude(),location.getLongitude(),1);
                    if (address == null){
                        address = getAddressLine(getContext(),location.getLatitude(),location.getLongitude(),1);
                    }
                }
            }

            if (address != null){
                binding.getLocationBtn2.setText(address);
                locationAddress = address;
            }
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
