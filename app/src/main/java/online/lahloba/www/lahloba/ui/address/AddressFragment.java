package online.lahloba.www.lahloba.ui.address;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import online.lahloba.www.lahloba.ui.interfaces.EditAddressFromFragmentListener;
import online.lahloba.www.lahloba.utils.Injector;

public class AddressFragment extends Fragment implements AddressAdapter.EditAddressClickListener {
    AddressViewModel mViewModel;
    RecyclerView addressRV;
    LinearLayoutManager linearLayoutManager;
    AddressAdapter addressAdapter;
    List<AddressItem> addressItems;
    private AddressItem defaultAddress;

    EditAddressFromFragmentListener editAddressFromFragmentListener;


    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddressBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_address, container, false);
        View view = binding.getRoot();

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
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
        addressAdapter.setEditAddressClickListener(this);
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

    @Override
    public void onEditAddressClicked(AddressItem addressItem) {
        editAddressFromFragmentListener.onClickEditAddressFromFragment(addressItem);
    }


    public void setEditAddressFromFragmentListener(EditAddressFromFragmentListener editAddressFromFragmentListener) {
        this.editAddressFromFragmentListener = editAddressFromFragmentListener;
    }
}
