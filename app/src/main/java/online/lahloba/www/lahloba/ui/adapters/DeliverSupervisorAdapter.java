package online.lahloba.www.lahloba.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliverySupervisorRowOrderBinding;
import online.lahloba.www.lahloba.ui.delivery_supervisor.DeliverySupervisorMainViewModel;

public class DeliverSupervisorAdapter extends RecyclerView.Adapter<DeliverSupervisorAdapter.DeliverySupervisorHolder> {
    DeliverySupervisorMainViewModel mViewModel;
    List<OrderItem> orderItems;

    DeliverySupervisorAdapterClick listener;

    public DeliverSupervisorAdapter(Context context) {
        try {
            listener = (DeliverySupervisorAdapterClick) context;
        }catch (ClassCastException exption){
            throw new ClassCastException(context.getClass().getName() + " Must implements DeliverySupervisorAdapterClick.class");
        }
    }

    @NonNull
    @Override
    public DeliverySupervisorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliverySupervisorRowOrderBinding binding = DeliverySupervisorRowOrderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);




        return new DeliverySupervisorHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverySupervisorHolder holder, int position) {
        mViewModel.startGetMarketolace(orderItems.get(position).getMarketplaceId());
        holder.binding.setOrder(orderItems.get(position));


        mViewModel.getMarketPlace().observe((LifecycleOwner) holder.binding.getRoot().getContext(), marketPlace -> {
            if (marketPlace==null)return;
            if (orderItems.size() <= position)return;

            if (marketPlace.getId().equals(orderItems.get(position).getMarketplaceId())){
                holder.binding.setMarketplace(marketPlace);
            }
        });

        holder.binding.allocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeliverySupervisorAdapterClick(orderItems.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderItems == null ? 0 : orderItems.size();
    }

    class DeliverySupervisorHolder extends RecyclerView.ViewHolder{

        private final DeliverySupervisorRowOrderBinding binding;

        public DeliverySupervisorHolder(DeliverySupervisorRowOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setViewModel(DeliverySupervisorMainViewModel viewModel) {
        this.mViewModel = viewModel;
    }


    public interface DeliverySupervisorAdapterClick{
        void onDeliverySupervisorAdapterClick(OrderItem orderItem);
    }
}
