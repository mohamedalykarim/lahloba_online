package online.lahloba.www.lahloba.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliverySupervisorRowOrderBinding;

public class DeliverSupervisorAdapter extends RecyclerView.Adapter<DeliverSupervisorAdapter.DeliverySupervisorHolder> {
    List<OrderItem> orderItems;

    @NonNull
    @Override
    public DeliverySupervisorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliverySupervisorRowOrderBinding binding = DeliverySupervisorRowOrderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DeliverySupervisorHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverySupervisorHolder holder, int position) {
        holder.binding.setOrder(orderItems.get(position));

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
}
