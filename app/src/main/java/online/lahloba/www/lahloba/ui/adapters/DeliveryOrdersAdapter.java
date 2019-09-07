package online.lahloba.www.lahloba.ui.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.DeliveryRowOrderBinding;
import online.lahloba.www.lahloba.ui.delivery.DeliveryMainViewModel;
import online.lahloba.www.lahloba.utils.StatusUtils;

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


        holder.binding.catchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(holder.binding.getRoot().getContext())
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to catch the order?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mViewModel.changeOrderStatus(
                                        orderItems.get(position).getId(),
                                        orderItems.get(position).getCityId(),
                                        StatusUtils.ORDER_STATUS_ON_THE_WAY
                                );

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });


        holder.binding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(holder.binding.getRoot().getContext())
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to catch the order?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mViewModel.changeOrderStatus(
                                        orderItems.get(position).getId(),
                                        orderItems.get(position).getCityId(),
                                        StatusUtils.ORDER_STATUS_COMPLETED
                                );

                                int points = 0;
                                for (CartItem productItem : orderItems.get(position).getProducts().values()){
                                    points += productItem.getPoint();
                                }

                                mViewModel.startAddPointsToUser(orderItems.get(position).getUserId(), points);



                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



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
