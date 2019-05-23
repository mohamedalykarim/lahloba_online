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
import android.widget.Toast;

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

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.databinding.RowProductListBinding;
import online.lahloba.www.lahloba.utils.Injector;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    List<ProductItem> productItemList;
    String userId ="userId";

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowProductListBinding binding = RowProductListBinding.inflate(inflater,viewGroup, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        productItemList.get(i).setCount(0);
        ProductItem item = productItemList.get(i);
        productViewHolder.bind(item);
        productViewHolder.clicks(i, item);

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
                                    .placeholder(R.drawable.progress_animation)
                                    .into(productViewHolder.binding.productImage);
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return null == productItemList? 0 : productItemList.size() ;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        RowProductListBinding binding;
        public ProductViewHolder(RowProductListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductItem item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }

        public void clicks(int i, ProductItem item){


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
                                    if(cartItem.getProductId().equals( productItemList.get(i).getId())){
                                        productItemList.get(i).setCount(cartItem.getCount());
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }else {
                Injector.provideRepository(context).getSpecificCartItem(item.getId())
                .observe((LifecycleOwner) context, cartItem -> {
                    if (cartItem!= null){
                        productItemList.get(i).setCount(cartItem.getCount());
                    }else {
                        productItemList.get(i).setCount(0);
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
                    }else{
                        Injector.getExecuter().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                addtiCountInternal(item);
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

                    productItemList.get(i).setCount(1);

                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        addItemTofirebase(item);
                    }else{
                        Injector.getExecuter().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                addItemToInternal(item);
                            }
                        });
                    }



                }
            });


        }
    }

    private void removeFromCountFirebase(ProductItem item) {
        if(item.getCount()>0){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Cart")
                    .child(userId)
                    .child(item.getId()).child("count")
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

    private void removeFromCountInternal(ProductItem item){
        Injector.provideRepository(context).changeCartItemCountInternaldb(item.getId(),item.getCount()-1);
    }

    private void addToCountFirebase(ProductItem item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart")
                .child(userId)
                .child(item.getId()).child("count")
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

    private void addtiCountInternal(ProductItem item) {
        Injector.provideRepository(context).changeCartItemCountInternaldb(item.getId(),item.getCount()+1);
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void addItemTofirebase(ProductItem item){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Cart");

        String push = databaseReference.push().getKey();
        CartItem cartItem = new CartItem();
        cartItem.setId(push);
        cartItem.setCount(1);
        cartItem.setProductId(item.getId());
        cartItem.setImage(item.getImage());
        cartItem.setPrice(item.getPrice());
        cartItem.setProductName(item.getTitle());

        databaseReference.child(userId).child(item.getId())
                .setValue(cartItem);
    }

    private void addItemToInternal(ProductItem item) {
        CartItemRoom cartItem = new CartItemRoom();
        cartItem.setCount(1);
        cartItem.setProductId(item.getId());
        cartItem.setImage(item.getImage());
        cartItem.setPrice(item.getPrice());
        cartItem.setProductName(item.getTitle());
        cartItem.setCurrency("EGP");

        Injector.provideRepository(context).insertCartItemToInternaldb(cartItem);


    }
}
