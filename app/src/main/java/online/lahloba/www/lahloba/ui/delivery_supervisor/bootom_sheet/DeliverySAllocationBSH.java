package online.lahloba.www.lahloba.ui.delivery_supervisor.bootom_sheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliverySupervisorAllocationBshBinding;
import online.lahloba.www.lahloba.ui.adapters.DeliverySDeliveryAdapter;
import online.lahloba.www.lahloba.ui.delivery_supervisor.DeliverySupervisorMainViewModel;
import online.lahloba.www.lahloba.utils.StatusUtils;

public class DeliverySAllocationBSH extends BottomSheetDialogFragment {
    private OrderItem orderItem;
    private List<String> deliveryIds;
    private boolean delivaryAvailable;
    private DeliverySDeliveryAdapter adapter;
    private DeliverySupervisorMainViewModel mViewModel;
    private DeliverySupervisorAllocationBshBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DeliverySupervisorAllocationBshBinding
                .inflate(inflater, container, false);



        deliveryIds = new ArrayList<>();

        adapter = new DeliverySDeliveryAdapter(getContext());
        adapter.setDeliveryIds(deliveryIds);
        adapter.setmViewModel(mViewModel);
        adapter.setOrderItem(orderItem);


        binding.deliveryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.deliveryRV.setAdapter(adapter);

        mViewModel.startGetDeliveriesForCity(orderItem.getCityId(), StatusUtils.DELIVERY_AREA_TYPE_DELIVERY);



        return binding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getDeliveriesId().observe(this,deliveryIdsList->{
            deliveryIds.clear();
            adapter.notifyDataSetChanged();
            delivaryAvailable = false;
            binding.setDeliveryAvailable(delivaryAvailable);



            if (deliveryIdsList== null)return;

            delivaryAvailable = true;
            binding.setDeliveryAvailable(delivaryAvailable);


            deliveryIds.addAll(deliveryIdsList);
            adapter.notifyDataSetChanged();
        });

    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setPeekHeight(0);

            }
        });

        return dialog;
    }


    @Override
    public void onPause() {
        super.onPause();

        mViewModel.clearDeliveriesIdForCity();
        mViewModel.startGetUserDetails(FirebaseAuth.getInstance().getUid());

    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public void setmViewModel(DeliverySupervisorMainViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
