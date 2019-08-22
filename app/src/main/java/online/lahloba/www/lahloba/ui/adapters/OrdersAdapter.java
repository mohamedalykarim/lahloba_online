package online.lahloba.www.lahloba.ui.adapters;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.RowOrderListBinding;
import online.lahloba.www.lahloba.ui.order.OrderDetailsActivity;
import online.lahloba.www.lahloba.ui.order.OrdersViewModel;
import online.lahloba.www.lahloba.utils.Constants;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    List<OrderItem> orderItemList;
    RowOrderListBinding binding;
    private OrdersViewModel mViewModel;


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        binding = RowOrderListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int i) {

        OrderItem orderItem = orderItemList.get(i);
        holder.binding.setOrder(orderItem);


        holder.binding.reorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel != null){
                    mViewModel.startReorder(orderItem);
                }

            }
        });

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_ID, orderItem.getId());
                intent.putExtra(Constants.ORDER_ITEM, orderItem);

                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });


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

    public void setViewModel(OrdersViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
