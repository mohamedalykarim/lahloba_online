package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.FragmentAddressBinding;
import online.lahloba.www.lahloba.ui.address.AddAddressActivity;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;

public class AddressFragment extends Fragment {

    private AddressViewModel mViewModel;

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
