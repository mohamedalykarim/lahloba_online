package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.FragmentAddressBinding;
import online.lahloba.www.lahloba.ui.adapters.AddressAdapter;
import online.lahloba.www.lahloba.ui.address.AddAddressActivity;
import online.lahloba.www.lahloba.ui.address.AddressActivity;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class AddressFragment extends Fragment {
    private AddressViewModel mViewModel;
    RecyclerView addressRV;
    LinearLayoutManager linearLayoutManager;
    AddressAdapter addressAdapter;
    List<AddressItem> addressItems;
    private AddressItem defaultAddress;

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddressBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_address, container, false);
        View view = binding.getRoot();

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this,factory).get(AddressViewModel.class);


        binding.addaddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAddressActivity.class);
                startActivity(intent);
            }
        });


        addressItems = new ArrayList<>();
        addressRV = binding.addressRV;
        linearLayoutManager = new LinearLayoutManager(getContext());
        addressAdapter = new AddressAdapter(mViewModel);
        addressRV.setLayoutManager(linearLayoutManager);
        addressRV.setAdapter(addressAdapter);

        mViewModel.startGetDefaultAddress(FirebaseAuth.getInstance().getCurrentUser().getUid());



        mViewModel.getDefaultAddress().observe(this, defaultAddress->{
            if (null != defaultAddress){
                this.defaultAddress = defaultAddress;
            }

        });

        addressAdapter.setAddressItemList(addressItems);






        binding.toolbar.setTitle("Addresses");
        binding.toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        ((AddressActivity)getActivity()).setSupportActionBar(binding.toolbar);


        mViewModel.getAddrresses().observe(this, address->{
            addressItems.clear();
            addressAdapter.notifyDataSetChanged();

            addressItems.addAll(address);
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.startGetAddrresses(FirebaseAuth.getInstance().getCurrentUser().getUid());


    }
}
