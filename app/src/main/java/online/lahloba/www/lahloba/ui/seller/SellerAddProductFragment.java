package online.lahloba.www.lahloba.ui.seller;

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
import online.lahloba.www.lahloba.databinding.SellerAddProductFragmentBinding;
import online.lahloba.www.lahloba.utils.Injector;

public class SellerAddProductFragment extends Fragment {

    private SellerAddProductViewModel mViewModel;

    public static SellerAddProductFragment newInstance() {
        return new SellerAddProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SellerAddProductFragmentBinding binding = SellerAddProductFragmentBinding.inflate(inflater,container,false);


        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerAddProductViewModel.class);

        return binding.getRoot();
    }


}
