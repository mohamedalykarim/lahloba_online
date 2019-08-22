package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.SellerAddProductFragmentBinding;
import online.lahloba.www.lahloba.utils.Injector;


public class SellerAddProductFragment extends Fragment {
    SellerAddProductFragmentBinding binding;

    private SellerAddProductViewModel mViewModel;

    public static SellerAddProductFragment newInstance() {
        return new SellerAddProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SellerAddProductFragmentBinding.inflate(inflater,container,false);


        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerAddProductViewModel.class);

        binding.imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return binding.getRoot();
    }


}
