package online.lahloba.www.lahloba.ui.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.databinding.RowCartListBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<CartItem> cartItemList;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowCartListBinding binding = RowCartListBinding.inflate(inflater,viewGroup, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder viewHolder, int i) {
        CartItem item = cartItemList.get(i);
        viewHolder.bind(item);
        viewHolder.clicks(i, item);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(item.getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get()
                                    .load(task.getResult())
                                    .into(viewHolder.binding.cartImage);
                        }
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

        public void bind(CartItem item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }

        public void clicks(int i, CartItem item){

//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//            databaseReference.child("Cart")
//                    .child("userId")
//                    .child(item.getId())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            if(null != dataSnapshot.getValue()){
//                                CartItem cartItem = dataSnapshot.getValue(CartItem.class);
//                                if(cartItem.getProductId().equals( productItemList.get(i).getId())){
//                                    productItemList.get(i).setCount(cartItem.getCount());
//                                }
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//            binding.minusContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int productCount = productItemList.get(i).getCount();
//                    if(productCount>0){
//
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                        databaseReference.child("Cart")
//                                .child("userId")
//                                .child(item.getId()).child("count")
//                                .runTransaction(new Transaction.Handler() {
//                                    @NonNull
//                                    @Override
//                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                        Object count = mutableData.getValue();
//
//                                        if(null == count ){
//                                            return Transaction.success(mutableData);
//                                        }else{
//                                            int countInt = Integer.parseInt("0"+count);
//                                            if(countInt > 0){
//                                                mutableData.setValue(countInt - 1);
//                                                return Transaction.success(mutableData);
//                                            }
//
//                                            return Transaction.success(mutableData);
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                    }
//                                });
//                    }
//                }
//            });
//
//            binding.plusContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int productCount = productItemList.get(i).getCount();
//
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                    databaseReference.child("Cart")
//                            .child("userId")
//                            .child(item.getId()).child("count")
//                            .runTransaction(new Transaction.Handler() {
//                                @NonNull
//                                @Override
//                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                    Object count = mutableData.getValue();
//
//                                    if(null == count ){
//                                        return Transaction.success(mutableData);
//                                    }else {
//                                        int countInt = Integer.parseInt("0"+count);
//                                        mutableData.setValue(countInt + 1);
//
//                                        return Transaction.success(mutableData);
//                                    }
//
//                                }
//
//                                @Override
//                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                }
//                            });
//                }
//            });
//
//            binding.addContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    binding.plusMinusContainer.setVisibility(View.VISIBLE);
//                    binding.addContainer.setVisibility(View.GONE);
//                    int count = productItemList.get(i).getCount();
//                    productItemList.get(i).setCount(count + 1);
//
//
//
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
//                            .getReference().child("Cart");
//
//                    String push = databaseReference.push().getKey();
//                    CartItem cartItem = new CartItem();
//                    cartItem.setId(push);
//                    cartItem.setCount(1);
//                    cartItem.setProductId(item.getId());
//                    cartItem.setImage(item.getImage());
//                    cartItem.setPrice(item.getPrice());
//                    cartItem.setProductName(item.getTitle());
//
//                    databaseReference.child("userId").child(item.getId())
//                            .setValue(cartItem);
//
//
//
//                }
//            });
//

        }
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }
}
