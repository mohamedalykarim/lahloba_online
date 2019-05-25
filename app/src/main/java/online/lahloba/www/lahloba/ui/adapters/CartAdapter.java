package online.lahloba.www.lahloba.ui.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.databinding.RowCartListBinding;
import online.lahloba.www.lahloba.ui.cart.CartActivity;
import online.lahloba.www.lahloba.utils.Injector;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    List<CartItem> cartItemList;
    String userId ="userId";

    public CartAdapter(Context context) {
        this.context = context;
    }

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


            /**
             * Read Count Value
             */
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Cart")
                        .child(userId)
                        .child(item.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(null != dataSnapshot.getValue()){
                                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                                    if(cartItem.getProductId().equals( cartItemList.get(i).getId())){
                                        cartItemList.get(i).setCount(cartItem.getCount());
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }else {
                Injector.provideRepository(context).getSpecificCartItem(item.getProductId())
                        .observe((LifecycleOwner) context, cartItem -> {
                            if (cartItem!= null){
                                cartItemList.get(i).setCount(cartItem.getCount());
                            }else {
                                cartItemList.get(i).setCount(0);
                            }
                        });
            }


            /**
             *  Minus Button
             */

            binding.minusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        removeFromCountFirebase(item);
                    }else {
                        Injector.getExecuter().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (item.getCount() >0){
                                    if (item.getCount() == 1){
                                        removeFromCountInternal(item);
                                        Injector.provideRepository(context).deleteSpecificCartItem(item.getId());
                                    }else {
                                        removeFromCountInternal(item);
                                    }

                                }

                            }
                        });
                    }
                }
            });


            /**
             * Plus Button
             */
            binding.plusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        addToCountFirebase(item);
                        Log.v("mmm","add to count firebase");
                    }else{
                        Injector.getExecuter().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("mmm","add to count internal");

                                addToCountInternal(item);
                            }
                        });
                    }
                }
            });


            /**
             * Add To Cart
             */
            binding.addContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cartItemList.get(i).setCount(1);

                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        addItemTofirebase(item);
                    }else{
                        Injector.getExecuter().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("mmm","add item to internal");

                                addItemToInternal(item);
                            }
                        });
                    }



                }
            });

        }
    }

    private void removeFromCountFirebase(CartItem item) {
        if(item.getCount()>0){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Cart")
                    .child(userId)
                    .child(item.getProductId()).child("count")
                    .runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Object count = mutableData.getValue();

                            if(null == count ){
                                return Transaction.success(mutableData);
                            }else{
                                int countInt = Integer.parseInt("0"+count);
                                if(countInt > 0){
                                    mutableData.setValue(countInt - 1);
                                    return Transaction.success(mutableData);
                                }

                                return Transaction.success(mutableData);
                            }

                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        }
                    });
        }
    }

    private void removeFromCountInternal(CartItem item){
        Injector.provideRepository(context).changeCartItemCountInternaldb(item.getProductId(),item.getCount()-1);
    }

    private void addToCountFirebase(CartItem item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart")
                .child(userId)
                .child(item.getProductId()).child("count")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Object count = mutableData.getValue();

                        if(null == count ){
                            return Transaction.success(mutableData);
                        }else {
                            int countInt = Integer.parseInt("0"+count);
                            mutableData.setValue(countInt + 1);

                            return Transaction.success(mutableData);
                        }

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    }
                });
    }

    private void addToCountInternal(CartItem item) {
        Injector.provideRepository(context).changeCartItemCountInternaldb(item.getProductId(),item.getCount()+1);
    }


    public void addItemTofirebase(CartItem item){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Cart");


        databaseReference.child(userId).child(item.getProductId())
                .setValue(item);
    }


    private void addItemToInternal(CartItem item) {
        CartItemRoom cartItem = new CartItemRoom();
        cartItem.setCount(1);
        cartItem.setProductId(item.getProductId());
        cartItem.setImage(item.getImage());
        cartItem.setPrice(item.getPrice());
        cartItem.setProductName(item.getProductName());
        cartItem.setCurrency("EGP");





        Injector.provideRepository(context).insertCartItemToInternaldb(cartItem);


    }


    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
