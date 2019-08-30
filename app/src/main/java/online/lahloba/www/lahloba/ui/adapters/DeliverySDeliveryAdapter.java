package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliverySupervisorDeliveryRowBinding;
import online.lahloba.www.lahloba.ui.delivery_supervisor.DeliverySupervisorMainViewModel;
import online.lahloba.www.lahloba.ui.delivery_supervisor.bootom_sheet.DeliverySAllocationBSH;
import online.lahloba.www.lahloba.utils.StatusUtils;

public class DeliverySDeliveryAdapter extends RecyclerView.Adapter<DeliverySDeliveryAdapter.DeliverySDeliveryViewHolder> {
    List<String> deliveryIds;
    DeliverySupervisorMainViewModel mViewModel;
    Context context;
    OrderItem orderItem;
    DeliverySDeliveryAdapterClick listener;

    public DeliverySDeliveryAdapter(Context context) {
        this.context = context;
        try {
            listener = (DeliverySDeliveryAdapterClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.getClass().getName() +" Must implements DeliverySDeliveryAdapterClick.class");
        }
    }

    @NonNull
    @Override
    public DeliverySDeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliverySupervisorDeliveryRowBinding binding = DeliverySupervisorDeliveryRowBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);



        return new DeliverySDeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverySDeliveryViewHolder holder, int position) {
        mViewModel.startGetUserDetails(deliveryIds.get(position));

        mViewModel.getUserDetails().observe((LifecycleOwner) context, userItem -> {
            if (userItem== null)return;
            if (deliveryIds.size() <= position)return;

            if (userItem.getId().equals(deliveryIds.get(position))){
                holder.binding.setUser(userItem);
            }
        });


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItem.setOrderStatus(StatusUtils.ORDER_STATUS_ALLOCATE_TO_DELIVERY);
                orderItem.setCityIdStatus(orderItem.getCityId()+"-"+StatusUtils.ORDER_STATUS_ALLOCATE_TO_DELIVERY   );
                orderItem.setDeliveryAllocatedTo(deliveryIds.get(position));
                mViewModel.updateOrder(orderItem);
                listener.onDeliverySDeliveryAdapterClick();
            }
        });
    }


    @Override
    public int getItemCount() {
        return deliveryIds == null ? 0: deliveryIds.size();
    }

    class DeliverySDeliveryViewHolder extends RecyclerView.ViewHolder{

        private final DeliverySupervisorDeliveryRowBinding binding;

        public DeliverySDeliveryViewHolder(DeliverySupervisorDeliveryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public void setDeliveryIds(List<String> deliveryIds) {
        this.deliveryIds = deliveryIds;
    }

    public void setmViewModel(DeliverySupervisorMainViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public interface DeliverySDeliveryAdapterClick{
        void onDeliverySDeliveryAdapterClick();
    }
}
