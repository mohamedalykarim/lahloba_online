package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.ProductOption;
import online.lahloba.www.lahloba.databinding.RowCartListBinding;
import online.lahloba.www.lahloba.databinding.RowProductListBinding;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.ui.products.ProductDetailsActivity;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.utils.Constants;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    List<CartItem> cartItemList;
    String userId ="userId";
    private CartViewModel mViewModel;


    public CartAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowCartListBinding binding = RowCartListBinding.inflate(inflater,viewGroup, false);

        if (FirebaseAuth.getInstance().getUid() != null){
            userId = FirebaseAuth.getInstance().getUid();
        }





        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.binding.optionsContainer.removeAllViews();

        mViewModel.startGetFavoriteItem(cartItemList.get(position).getProductId());
        mViewModel.startGetProductItem(cartItemList.get(position).getProductId());


        if (cartItemList.get(position).getOptions() != null){
            Iterator it = cartItemList.get(position).getOptions().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry entry = (Map.Entry) it.next();

                TextView textView = new TextView(holder.binding.getRoot().getContext());
                textView.setPadding(8,10,10,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,0,0,0);
                textView.setLayoutParams(params);


                textView.setText(((ProductOption)entry.getValue()).getOptionKey());


                holder.binding.optionsContainer.addView(textView);

                it.remove();
            }
        }

        /**
         * Read cart item
         */

        if (FirebaseAuth.getInstance().getUid() == null){

            mViewModel.getSpecificCartItemFromInternal(cartItemList.get(position).getProductId())
                    .observe((LifecycleOwner) context, item -> {
                        if (item == null) return;
                        holder.binding.setCartItem(item);
                    });
        }else {
            holder.binding.setCartItem(cartItemList.get(position));
        }

        /**
         * Read product item
         */

        mViewModel.getProductItem().observe((LifecycleOwner) holder.binding.getRoot().getContext(), productItem -> {
            if (productItem == null)return;
            holder.binding.setItem(productItem);
        });



        /**
         * add to count
         */

        holder.binding.plusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null){
                    mViewModel.addToCartItemCountInternaldb(cartItemList.get(position).getProductId());


                }else{
                    mViewModel.startAddToCartProductCount(cartItemList.get(position).getProductId());
                }
            }
        });



        /**
         * remove from count
         */

        holder.binding.minusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null){

                    mViewModel.removeFromCartItemCountInternaldb(cartItemList.get(position).getProductId());

                }else{
                    mViewModel.startRemoveFromCartProductCount(cartItemList.get(position).getProductId());
                }
            }
        });


        /**
         * favorite
         */

        holder.binding.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() != null){
                    mViewModel.startChangeFavoriteStatus(cartItemList.get(position).getProductId());
                }
            }
        });


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), ProductDetailsActivity.class);
                intent.putExtra(Constants.PRODUCT_ID, cartItemList.get(position).getProductId());
                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });





        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child(cartItemList.get(position).getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get()
                                    .load(task.getResult())
                                    .placeholder(R.drawable.progress_animation)
                                    .into(holder.binding.productImage);
                        }
                    }
                });





        mViewModel.getFavoritesItem().observe((LifecycleOwner) context, favoriteItem -> {
            if (cartItemList == null) return;
            if (cartItemList.size() == 0)return;
            if (favoriteItem.getProductId()==null)return;

            if (favoriteItem.getProductId().equals(cartItemList.get(position).getProductId())){
                holder.binding.setFavorite(favoriteItem);
            }
        });


    }


    @Override
    public int getItemCount() {
        return null == cartItemList? 0 : cartItemList.size() ;
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        RowCartListBinding binding;
        public CartViewHolder(RowCartListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setmViewModel(CartViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
