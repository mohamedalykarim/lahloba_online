package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.FragmentAddressBinding;
import online.lahloba.www.lahloba.ui.adapters.AddressAdapter;
import online.lahloba.www.lahloba.ui.address.AddAddressActivity;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;

public class AddressFragment extends Fragment {
    private AddressViewModel mViewModel;
    RecyclerView addressRV;
    LinearLayoutManager linearLayoutManager;
    AddressAdapter addressAdapter;
    List<AddressItem> addressItems;

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddressBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_address, container, false);
        View view = binding.getRoot();

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
        addressAdapter = new AddressAdapter();
        addressRV.setLayoutManager(linearLayoutManager);
        addressRV.setAdapter(addressAdapter);

        for (int i=0; i<5; i++){
            AddressItem addressItem = new AddressItem();
            addressItem.setName("Mohamed ALi");
            addressItem.setFlatNumber(15);
            addressItem.setFloor(16);
            addressItem.setBuilding("Atone Building");
            addressItem.setStreet("Khaled Ebn Elwalied ST.");
            addressItem.setZone("Awamia");
            addressItem.setCity("Luxor");
            addressItem.setCountry("Egypt");

            addressItems.add(addressItem);
        }
        addressAdapter.setAddressItemList(addressItems);
        addressAdapter.notifyDataSetChanged();





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory factory = new ViewModelProviderFactory(null,null);
        mViewModel = ViewModelProviders.of(this,factory).get(AddressViewModel.class);
        // TODO: Use the ViewModel
    }

}
