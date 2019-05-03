package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.ui.address.AddAddressViewModel;

public class AddAddressFragment extends Fragment {

    private AddAddressViewModel mViewModel;

    public static AddAddressFragment newInstance() {
        return new AddAddressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_address, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory factory = new ViewModelProviderFactory(null,null);
        mViewModel = ViewModelProviders.of(this,factory).get(AddAddressViewModel.class);
        // TODO: Use the ViewModel
    }

}
