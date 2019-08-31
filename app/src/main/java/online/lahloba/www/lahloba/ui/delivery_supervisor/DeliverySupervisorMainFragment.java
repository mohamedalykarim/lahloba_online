package online.lahloba.www.lahloba.ui.delivery_supervisor;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.DeliveryArea;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliverySupervisorMainFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.CustomSpinnerAdapter;
import online.lahloba.www.lahloba.ui.adapters.DeliverSupervisorAdapter;
import online.lahloba.www.lahloba.ui.delivery_supervisor.bootom_sheet.DeliverySAllocationBSH;
import online.lahloba.www.lahloba.utils.Injector;

public class DeliverySupervisorMainFragment extends Fragment {

    private DeliverySupervisorMainViewModel mViewModel;
    private DeliverySupervisorMainFragmentBinding binding;
    List<String> citySpinnerIds;

    DeliverSupervisorAdapter adapter;
    List<OrderItem> orderItems;
    private DeliverySAllocationBSH deliverySAllocationBSH;


    public static DeliverySupervisorMainFragment newInstance() {
        return new DeliverySupervisorMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DeliverySupervisorMainFragmentBinding
                .inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this,factory).get(DeliverySupervisorMainViewModel.class);
        mViewModel.startGetDeliveryAreas();

        citySpinnerIds = new ArrayList<>();


        /**
         * RecyclerView
         * Adapter
         */

        orderItems = new ArrayList<>();

        adapter = new DeliverSupervisorAdapter(getContext());
        adapter.setOrderItems(orderItems);
        adapter.setViewModel(mViewModel);

        binding.orderRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.orderRv.setAdapter(adapter);


        binding.citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderItems.clear();
                adapter.notifyDataSetChanged();
                mViewModel.startGetOrdersForDeliverysupervisor(citySpinnerIds.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getDeliveryAreas().observe(this, deliveryAreas -> {
            if (deliveryAreas==null)return;


            List<String> areasName = new ArrayList<>();
            for (DeliveryArea cityItem : deliveryAreas){
                areasName.add(cityItem.getName());
                citySpinnerIds.add(cityItem.getId());
            }

            CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(
                 getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                 areasName
            );

            mViewModel.startGetOrdersForDeliverysupervisor(deliveryAreas.get(0).getId());


            binding.citiesSpinner.setAdapter(spinnerAdapter);

        });


        mViewModel.getOrders().observe(this, orders -> {
            if (orders==null)return;
            if (orders.size() == 0)return;
            if (citySpinnerIds == null)return;

            orderItems.clear();


            if (!orders.get(0).getCityId().equals(citySpinnerIds.get(binding.citiesSpinner.getSelectedItemPosition())))
                return;


            orderItems.addAll(orders);
            adapter.notifyDataSetChanged();
        });
    }

    public void onDeliverySupervisorAdapterClick(OrderItem orderItem) {
        deliverySAllocationBSH = new DeliverySAllocationBSH();
        deliverySAllocationBSH.setOrderItem(orderItem);
        deliverySAllocationBSH.setmViewModel(mViewModel);
        deliverySAllocationBSH.show(getFragmentManager(), "");

    }

    public void onDeliverySDeliveryAdapterClick() {
        deliverySAllocationBSH.dismiss();
    }
}
