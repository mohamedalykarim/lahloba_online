package online.lahloba.www.lahloba.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.DeliveryRowOrderBinding;
import online.lahloba.www.lahloba.ui.delivery.DeliveryMainViewModel;
import online.lahloba.www.lahloba.utils.OnSwipeTouchListener;

public class DeliveryOrdersAdapter extends RecyclerView.Adapter<DeliveryOrdersAdapter.DeliveryOrdersViewHolder> {
    List<OrderItem> orderItems;
    DeliveryMainViewModel mViewModel;

    @NonNull
    @Override
    public DeliveryOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliveryRowOrderBinding binding = DeliveryRowOrderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new DeliveryOrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryOrdersViewHolder holder, int position) {
        holder.binding.setOrder(orderItems.get(position));
        mViewModel.startGetMarketplaceForOrder(orderItems.get(position).getMarketplaceId());

        mViewModel.getMarketPlace().observe((LifecycleOwner) holder.binding.getRoot().getContext(), marketPlace -> {
            if (marketPlace==null)return;
            if (orderItems.size() <= position)return;

            if (marketPlace.getId().equals(orderItems.get(position).getMarketplaceId())){
                holder.binding.setMarketplace(marketPlace);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderItems == null ? 0 : orderItems.size();
    }

    class DeliveryOrdersViewHolder extends RecyclerView.ViewHolder{

        private final DeliveryRowOrderBinding binding;

        public DeliveryOrdersViewHolder(DeliveryRowOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setmViewModel(DeliveryMainViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
