package online.lahloba.www.lahloba.ui.adapters;

import androidx.lifecycle.LifecycleOwner;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.HashMap;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.RowCartListBinding;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.LocalUtils;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    List<CartItem> cartItemList;
    String userId;
    private CartViewModel cartViewModel;

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

        if (FirebaseAuth.getInstance().getUid() != null){
            userId = FirebaseAuth.getInstance().getUid();
        }


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


        favorites(viewHolder,i,item);



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

            String language = LocalUtils.getLangauge();
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            mRef.child("Product").child(language).child(item.getProductId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                ProductItem productItem = dataSnapshot.getValue(ProductItem.class);
                                Log.v("mmm", productItem.isStatus()+"");
                                if (!productItem.isStatus()){
                                    mRef.child("Cart").child(userId)
                                            .child("CartItems")
                                            .child(item.getProductId())
                                            .removeValue();


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




            /**
             * Read Count Value
             */
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                mRef.child("Cart")
                        .child(userId)
                        .child("CartItems")
                        .child(item.getProductId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(null != dataSnapshot.getValue()){
                                    if (cartItemList.size() != 0){
                                        CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                                        if(cartItem.getProductId().equals( cartItemList.get(i).getId())){
                                            cartItemList.get(i).setCount(cartItem.getCount());
                                        }
                                    }



                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }else {
                Injector.provideRepository(context).getSpecificCartItemFromInternal(item.getProductId())
                        .observe((LifecycleOwner) context, cartItem -> {
                            if (cartItem!= null){
                                cartItemList.get(i).setCount(cartItem.getCount());
                            }else {
                                if(cartItemList.size() > 0)
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
                    String langauge = LocalUtils.getLangauge();

                    mRef.child("Product")
                            .child(langauge)
                            .child(item.getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ProductItem productItem = dataSnapshot.getValue(ProductItem.class);
                                        if (productItem.isStatus()){


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
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                }
            });


            /**
             * Plus Button
             */
            binding.plusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String langauge = LocalUtils.getLangauge();

                    mRef.child("Product")
                            .child(langauge)
                            .child(item.getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ProductItem productItem = dataSnapshot.getValue(ProductItem.class);
                                        if (productItem.isStatus()){

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
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });







                }
            });


            /**
             * Add To Cart
             */
            binding.addContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String langauge = LocalUtils.getLangauge();

                    mRef.child("Product")
                            .child(langauge)
                            .child(item.getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ProductItem productItem = dataSnapshot.getValue(ProductItem.class);
                                        if (productItem.isStatus()){


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
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });







                }
            });

        }
    }


    private void favorites(@NonNull CartViewHolder holder, int i, CartItem item) {
        if (userId == null)return;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Favorites")
                .child(userId)
                .child(item.getProductId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            Picasso.get().load(R.drawable.favorite_icon).into(holder.binding.favoriteImage);
                            cartItemList.get(i).setFavorite(true);
                        }else{
                            Picasso.get().load(R.drawable.no_favorite_icon).into(holder.binding.favoriteImage);
                            cartItemList.get(i).setFavorite(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        holder.binding.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemList.get(i).isFavorite()){
                    databaseReference.child("Favorites")
                            .child(userId)
                            .child(item.getProductId()).removeValue();
                }else {
                    databaseReference.child("Favorites")
                            .child(userId)
                            .child(item.getProductId()).setValue(item);
                }
            }
        });
    }


    private void removeFromCountFirebase(CartItem item) {
        if(item.getCount()>0){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Cart")
                    .child(userId)
                    .child("CartItems")
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
        Injector.provideRepository(context).removeFromCartItemCountInternaldb(item.getProductId());
    }

    private void addToCountFirebase(CartItem item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart")
                .child(userId)
                .child("CartItems")
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
        Injector.provideRepository(context).addToCartItemCountInternaldb(item.getProductId());
    }


    public void addItemTofirebase(CartItem item){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Cart");


        databaseReference.child(userId).child("CartLocation")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren()){
                            /**
                             * Cart Location is set
                             */
                            Location cartLocation = new Location("");
                            cartLocation.setLatitude(Double.parseDouble(dataSnapshot.child("lat").getValue().toString()));
                            cartLocation.setLongitude(Double.parseDouble(dataSnapshot.child("lon").getValue().toString()));

                            Location myLocation = new Location("");
                            myLocation.setLatitude(Double.parseDouble(SharedPreferencesManager.getCurrentLocationLat(context)));
                            myLocation.setLongitude(Double.parseDouble(SharedPreferencesManager.getCurrentLocationLan(context)));

                            if (cartLocation.distanceTo(myLocation)/1000 < 30){
                                databaseReference.child(userId).child("CartItems").child(item.getId())
                                        .setValue(item);

                            }else{

                            }

                        }else{
                            /**
                             * Cart Location not set
                             */
                            databaseReference.child(userId).child("CartItems").child(item.getId())
                                    .setValue(item);

                            HashMap<String,Object> newCartLocation = new HashMap<>();
                            newCartLocation.put("lat", SharedPreferencesManager.getCurrentLocationLat(context));
                            newCartLocation.put("lon", SharedPreferencesManager.getCurrentLocationLan(context));

                            databaseReference.child(userId).child("CartLocation").setValue(newCartLocation);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }


    private void addItemToInternal(CartItem item) {

        Injector.provideRepository(context).insertCartItemToInternaldb(item);


    }


    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setCartViewModel(CartViewModel cartViewModel) {
        this.cartViewModel = cartViewModel;
    }


}
