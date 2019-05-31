package online.lahloba.www.lahloba.ui.order;

import android.arch.lifecycle.ViewModelProviders;
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
import online.lahloba.www.lahloba.databinding.FragmentOrdersBinding;
import online.lahloba.www.lahloba.utils.Injector;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentOrdersBinding binding = FragmentOrdersBinding.inflate(inflater,container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this,factory).get(OrdersViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.startGetCurrentOrders();
        mViewModel.getCurrentOrders().observe(this, orderItems -> {
            Toast.makeText(getContext(), ""+orderItems.size(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);    }

}
