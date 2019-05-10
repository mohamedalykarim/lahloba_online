package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.FragmentAddAddressBinding;
import online.lahloba.www.lahloba.ui.address.AddAddressViewModel;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class AddAddressFragment extends Fragment {

    private AddAddressViewModel mViewModel;
    FragmentAddAddressBinding binding;
    private int error = 0;
    private AddressViewModel addressViewModel;
    private AddressItem defaultAddress;


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

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this,factory).get(AddAddressViewModel.class);

        ViewModelProviderFactory addressFactory = Injector.getVMFactory(getContext(),null);
        addressViewModel = ViewModelProviders.of(this,addressFactory).get(AddressViewModel.class);



        mViewModel.setIsAddressAddedFalse();

        binding.addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
                if (error==0){
                    AddressItem addressItem = new AddressItem();
                    addressItem.setDefault(binding.switch1.isChecked());
                    addressItem.setName(binding.nameET.getText().toString());
                    addressItem.setCountry(binding.countryET.getText().toString());
                    addressItem.setCity(binding.cityET.getText().toString());
                    addressItem.setZone(binding.zoneET.getText().toString());
                    addressItem.setStreet(binding.streetET.getText().toString());
                    addressItem.setBuilding(binding.buildingET.getText().toString());
                    addressItem.setFloor(Integer.parseInt(binding.floorET.getText().toString()));
                    addressItem.setFlatNumber(Integer.parseInt(binding.flatNumberET.getText().toString()));



                    mViewModel.startAddNewAddress(
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            addressItem
                    );



                }
            }
        });

        mViewModel.getIsAddressAdded().observe(this, isAddressAdded->{
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
            binding.countryET.setError("Please Insert Country Name");
            error++;
        }

        if (binding.cityET.getText().length() < 1){
            binding.cityET.setError("Please Insert City Name");
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

        addressViewModel.getDefaultAddress().observe(this, defaultAddress->{
            this.defaultAddress = defaultAddress;
        });
    }
}
