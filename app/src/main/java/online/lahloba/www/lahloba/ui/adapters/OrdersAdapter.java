package online.lahloba.www.lahloba.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.RowOrderListBinding;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    List<OrderItem> orderItemList;

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowOrderListBinding binding = RowOrderListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return null == orderItemList? 0 : orderItemList.size() ;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        private final RowOrderListBinding binding;

        public OrderViewHolder(@NonNull RowOrderListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
