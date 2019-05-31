package online.lahloba.www.lahloba.ui.adapters;

import android.arch.persistence.room.PrimaryKey;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.databinding.RowOrderListBinding;
import online.lahloba.www.lahloba.databinding.RowOrderListProductBinding;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    List<OrderItem> orderItemList;

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowOrderListBinding binding = RowOrderListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setOrder(orderItemList.get(i));
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int i) {

        OrderItem orderItem = orderItemList.get(i);

        /*
         * Add Products to view
         */
        HashMap<String, CartItem> products = orderItem.getProducts();
        Iterator iterator = products.entrySet().iterator();
        while(iterator.hasNext()){

            Map.Entry entry = (Map.Entry) iterator.next();
            CartItem product = (CartItem) entry.getValue();

            if (product!=null){
                LayoutInflater inflater = LayoutInflater.from(holder.binding.getRoot().getContext());
                View view = inflater.inflate(R.layout.row_order_list_product,null,false);
                TextView nameTv = view.findViewById(R.id.productNameTv);
                TextView quantityTv = view.findViewById(R.id.productQuantityTv);
                TextView priceTv = view.findViewById(R.id.producPriceTv);

                nameTv.setText(product.getProductName());
                quantityTv.setText("Qty: "+product.getCount());
                priceTv.setText(product.getPrice()+" " + "EGP");


                holder.binding.productsContainer.addView(view);
            }
        }


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
