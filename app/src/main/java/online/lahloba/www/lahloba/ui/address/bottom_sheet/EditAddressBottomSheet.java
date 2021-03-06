package online.lahloba.www.lahloba.ui.address.bottom_sheet;

import android.Manifest;
import android.app.Dialog;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.BottomSheetEditAddressBinding;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;

public class EditAddressBottomSheet extends BottomSheetDialogFragment implements LocationListener {
    AddressViewModel addressViewModel;
    AddressItem addressItem;


    LocationManager mLocationManager;
    Location location;
    double lat, lon;
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetEditAddressBinding binding = BottomSheetEditAddressBinding.inflate(inflater,container,false);
        binding.setViewmodel(addressViewModel);

        binding.getLocationBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });



        addressViewModel.addressVMHelper.setEditedAddress(addressItem);

        binding.editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressViewModel.startEditAddress(addressViewModel.addressVMHelper.getEditedAddress());
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressViewModel.getIsAddressEdited().observe(getActivity(), isAddressEdited->{
            if (null == isAddressEdited) return;

            if (isAddressEdited){
                this.dismiss();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet =  d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }

    public void setAddressViewModel(AddressViewModel addressViewModel) {
        this.addressViewModel = addressViewModel;
    }

    public void setAddressItem(AddressItem addressItem) {
        this.addressItem = addressItem;
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
